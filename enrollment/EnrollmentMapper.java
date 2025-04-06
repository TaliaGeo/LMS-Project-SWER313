package com.example.lms.enrollment;

import com.example.lms.course.Course;
import com.example.lms.course.CourseRepository;
import com.example.lms.student.Student;
import com.example.lms.student.StudentRepository;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EnrollmentMapper {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public EnrollmentMapper(CourseRepository courseRepository,
                            StudentRepository studentRepository,
                            UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public EnrollmentDTO toDTO(Enrollment enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setStatus(enrollment.getStatus());
        dto.setProgress(enrollment.getProgress());
        dto.setCompleted(enrollment.getCompleted());

        if (enrollment.getCourse() != null) {
            dto.setCourseId(enrollment.getCourse().getId());
        }
        if (enrollment.getStudent() != null) {
            dto.setStudentId(enrollment.getStudent().getId());
        }
        if (enrollment.getUser() != null) {
            dto.setUserId(enrollment.getUser().getId());
        }

        return dto;
    }

    public Enrollment toEntity(EnrollmentDTO dto) {
        Enrollment enrollment = new Enrollment();
        enrollment.setStatus(dto.getStatus());
        enrollment.setProgress(dto.getProgress());
        enrollment.setCompleted(dto.getCompleted());

        if (dto.getCourseId() != null) {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new IllegalArgumentException("Course not found"));
            enrollment.setCourse(course);
        }
        if (dto.getStudentId() != null) {
            Student student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));
            enrollment.setStudent(student);
        }
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            enrollment.setUser(user);
        }

        return enrollment;
    }

    public void updateEntity(Enrollment enrollment, EnrollmentDTO dto) {
        enrollment.setStatus(dto.getStatus());
        enrollment.setProgress(dto.getProgress());
        enrollment.setCompleted(dto.getCompleted());

        if (dto.getCourseId() != null) {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new IllegalArgumentException("Course not found"));
            enrollment.setCourse(course);
        }
        if (dto.getStudentId() != null) {
            Student student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));
            enrollment.setStudent(student);
        }
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            enrollment.setUser(user);
        }
    }
}
