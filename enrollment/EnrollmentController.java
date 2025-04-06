package com.example.lms.enrollment;

import com.example.lms.admin.AdminController;
import com.example.lms.course.CourseController;
import com.example.lms.student.StudentController;
import com.example.lms.user.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @GetMapping
    public ResponseEntity<List<EntityModel<EnrollmentDTO>>> getAllEnrollments() {
        List<EnrollmentDTO> enrollments = enrollmentService.getAllEnrollments();
        List<EntityModel<EnrollmentDTO>> enrollmentModels = enrollments.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(enrollmentModels);
    }
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EntityModel<EnrollmentDTO>>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        List<EntityModel<EnrollmentDTO>> enrollmentModels = enrollments.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(enrollmentModels);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR') or hasRole('STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EnrollmentDTO>> getEnrollmentById(@PathVariable Long id) {
        EnrollmentDTO enrollmentDTO = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(toModel(enrollmentDTO));
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<EnrollmentDTO>> createEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        if (enrollmentDTO.getStudentId() == null || enrollmentDTO.getCourseId() == null) {
            throw new IllegalArgumentException("Student ID and Course ID are required.");
        }
        EnrollmentDTO created = enrollmentService.createEnrollment(enrollmentDTO);
        return ResponseEntity.ok(toModel(created));
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<EnrollmentDTO>> updateEnrollment(@PathVariable Long id,
            @RequestBody EnrollmentDTO enrollmentDTO) {
        EnrollmentDTO updated = enrollmentService.updateEnrollment(id, enrollmentDTO);
        return ResponseEntity.ok(toModel(updated));
    }

    private EntityModel<EnrollmentDTO> toModel(EnrollmentDTO enrollmentDTO) {
        EntityModel<EnrollmentDTO> model = EntityModel.of(enrollmentDTO);
        model.add(linkTo(methodOn(EnrollmentController.class).getEnrollmentById(enrollmentDTO.getId())).withSelfRel());

        if (enrollmentDTO.getStudentId() != null) {
            model.add(linkTo(methodOn(AdminController.class).getStudentById(enrollmentDTO.getStudentId()))
                    .withRel("student"));
        }
        if (enrollmentDTO.getCourseId() != null) {
            model.add(linkTo(methodOn(CourseController.class).getCourseById(enrollmentDTO.getCourseId()))
                    .withRel("course"));
        }
        if (enrollmentDTO.getUserId() != null) {
            model.add(linkTo(methodOn(UserController.class).getUserById(enrollmentDTO.getUserId())).withRel("user"));
        }

        return model;
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PutMapping("/progress/{studentId}/{courseId}")
    public ResponseEntity<String> updateProgress(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        enrollmentService.updateProgress(studentId, courseId);
        return ResponseEntity.ok("Progress updated successfully.");
    }
}
