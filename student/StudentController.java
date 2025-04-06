package com.example.lms.student;

import com.example.lms.course.Course;
import com.example.lms.course.CourseController;
import com.example.lms.enrollment.EnrollmentRepository;
import com.example.lms.student.StudentMapper.StudentDTO;
import com.example.lms.user.UserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    public StudentController(StudentService studentService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    private EntityModel<StudentDTO> toModel(Student student) {
        StudentDTO dto = studentMapper.toDTO(student);
        EntityModel<StudentDTO> studentModel = EntityModel.of(dto);

        studentModel.add(linkTo(methodOn(StudentController.class).getMyStudentInfo()).withSelfRel());
        if (student.getUser() != null && student.getUser().getId() != null) {
            studentModel.add(linkTo(methodOn(UserController.class).getUserById(student.getUser().getId())).withRel("user"));
        }
        return studentModel;
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EntityModel<StudentDTO>> updateMyProfile(@RequestBody StudentDTO studentDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Student student = studentService.getStudentByUsername(username);
        student.setMajor(studentDTO.getMajor());

        Student updated = studentService.updateStudent(student.getId(), student);
        return ResponseEntity.ok(toModel(updated));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EntityModel<StudentDTO>> getMyStudentInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Student student = studentService.getStudentByUsername(username);
        return ResponseEntity.ok(toModel(student));
    }


    @GetMapping("/me/courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EntityModel<Course>>> getMyCourses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Student student = studentService.getStudentByUsername(username);

        List<Course> courses = studentService.getCoursesForStudent(student.getId());
        List<EntityModel<Course>> courseModels = courses.stream()
                .map(course -> EntityModel.of(course,
                        linkTo(methodOn(CourseController.class).getCourseById(course.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(courseModels);
    }
}
