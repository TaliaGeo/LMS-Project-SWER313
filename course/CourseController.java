package com.example.lms.course;

import com.example.lms.instructor.InstructorController;
import com.example.lms.notification.NotificationDTO;
import com.example.lms.notification.NotificationService;
import com.example.lms.notification.NotificationType;
import com.example.lms.student.StudentService;
import com.example.lms.user.User;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.lms.admin.AdminController;
import com.example.lms.email.EmailService;
import com.example.lms.enrollment.EnrollmentService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;
    private final StudentService studentService;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final EnrollmentService enrollmentService;

    public CourseController(CourseService courseService,
            CourseMapper courseMapper,
            StudentService studentService,
            NotificationService notificationService,
            EmailService emailService,
            EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
        this.studentService = studentService;
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.enrollmentService = enrollmentService;
    }

    private EntityModel<CourseDTO> toModel(Course course) {
        CourseDTO dto = courseMapper.toDTO(course);
        EntityModel<CourseDTO> model = EntityModel.of(dto);

        model.add(linkTo(methodOn(CourseController.class).getCourseById(course.getId())).withSelfRel());
        model.add(linkTo(methodOn(CourseController.class).getAllCourses()).withRel("all-courses"));

        if (course.getInstructor() != null && course.getInstructor().getId() != null) {
            model.add(linkTo(methodOn(AdminController.class)
                    .getInstructorById(course.getInstructor().getId()))
                    .withRel("instructor"));
        }

        return model;
    }

    @GetMapping
   
    public ResponseEntity<List<EntityModel<CourseDTO>>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        List<EntityModel<CourseDTO>> models = courses.stream().map(this::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(models);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR') or hasRole('STUDENT')")
    public ResponseEntity<EntityModel<CourseDTO>> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(toModel(course));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EntityModel<CourseDTO>>> getCoursesForStudent(@PathVariable Long studentId) {
        List<Course> courses = studentService.getCoursesForStudent(studentId);
        List<EntityModel<CourseDTO>> models = courses.stream().map(this::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(models);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<EntityModel<CourseDTO>> createCourse(@RequestBody CourseDTO dto) {
        Course course = courseMapper.toEntity(dto);
        Course created = courseService.createCourse(course);
        return ResponseEntity.ok(toModel(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<EntityModel<CourseDTO>> updateCourse(@PathVariable Long id, @RequestBody CourseDTO dto) {
        Course existing = courseService.getCourseById(id);
        courseMapper.updateEntity(existing, dto);
        Course updated = courseService.updateCourse(id, existing);
        List<User> enrolledUsers = enrollmentService.getUsersByCourseId(id);

        for (User studentUser : enrolledUsers) {
            // Notification
            NotificationDTO notification = new NotificationDTO();
            notification.setTitle("Course Updated");
            notification.setMessage("The course '" + updated.getTitle() + "' has been updated.");
            notification.setType(NotificationType.COURSE_UPDATE);
            notification.setUserId(studentUser.getId());
            notificationService.createNotification(notification);

            // Email
            System.out.println("📧 Sending to: " + studentUser.getEmail());

            try {
                emailService.sendCourseUpdateEmail(
                    studentUser.getEmail(),
                    studentUser.getUsername(),
                    updated.getTitle());
            } catch (Exception e) {
                System.err.println("❌ Failed to send course update email: " + e.getMessage());
            }
        }            

        return ResponseEntity.ok(toModel(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
