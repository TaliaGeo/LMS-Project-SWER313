
package com.example.lms.enrollment;

import java.util.List;

import com.example.lms.user.User;

public interface EnrollmentService {
    List<EnrollmentDTO> getAllEnrollments();
    List<User> getUsersByCourseId(Long courseId);

    EnrollmentDTO getEnrollmentById(Long id);
    EnrollmentDTO createEnrollment(EnrollmentDTO enrollmentDTO);
    EnrollmentDTO updateEnrollment(Long id, EnrollmentDTO enrollmentDTO);
    void deleteEnrollment(Long id);
    void updateProgress(Long studentId, Long courseId);
    List<EnrollmentDTO> getEnrollmentsByStudent(Long studentId);
}
