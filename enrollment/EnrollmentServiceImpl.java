package com.example.lms.enrollment;

import com.example.lms.assessment.AssessmentRepository;
import com.example.lms.submission.SubmissionRepository;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import com.example.lms.exceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    @Autowired
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final AssessmentRepository assessmentRepository;
    private final SubmissionRepository submissionRepository;

    public EnrollmentServiceImpl(
            EnrollmentRepository enrollmentRepository,
            EnrollmentMapper enrollmentMapper,
            AssessmentRepository assessmentRepository,
            SubmissionRepository submissionRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentMapper = enrollmentMapper;
        this.assessmentRepository = assessmentRepository;
        this.submissionRepository = submissionRepository;
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getUsersByCourseId(Long courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        return enrollments.stream()
                .map(e -> e.getStudent().getUser())
                .filter(user -> user.getEmail() != null && !user.getEmail().isBlank())
                .toList();
    }

    @Override
    public List<EnrollmentDTO> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(this::updateProgressAndMapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentDTO getEnrollmentById(Long id) {
        Enrollment enrollment = findEnrollmentById(id);
        return updateProgressAndMapToDTO(enrollment);
    }

    @Override
    public EnrollmentDTO createEnrollment(EnrollmentDTO enrollmentDTO) {
        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentDTO);
        enrollment.setProgress(0.0);
        enrollment.setCompleted(false);
        enrollment.updateStatus();
        return updateProgressAndMapToDTO(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentDTO updateEnrollment(Long id, EnrollmentDTO enrollmentDTO) {
        Enrollment existingEnrollment = findEnrollmentById(id);
        enrollmentMapper.updateEntity(existingEnrollment, enrollmentDTO);
        existingEnrollment.updateStatus();
        return updateProgressAndMapToDTO(enrollmentRepository.save(existingEnrollment));
    }

    @Override
    public void deleteEnrollment(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enrollment not found with ID: " + id);
        }
        enrollmentRepository.deleteById(id);
    }

    @Override
    public void updateProgress(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment not found for student ID: " + studentId + " and course ID: " + courseId));

        double progress = calculateProgress(studentId, courseId);
        enrollment.setProgress(progress);
        enrollment.setCompleted(progress == 100.0);
        enrollment.updateStatus();
        enrollmentRepository.save(enrollment);
    }

    private EnrollmentDTO updateProgressAndMapToDTO(Enrollment enrollment) {
        if (enrollment.getStudent() == null || enrollment.getCourse() == null) {
            throw new ResourceNotFoundException("Enrollment is missing student or course information.");
        }

        double progress = calculateProgress(enrollment.getStudent().getId(), enrollment.getCourse().getId());
        enrollment.setProgress(progress);
        enrollment.setCompleted(progress == 100);
        enrollment.updateStatus();
        enrollmentRepository.save(enrollment);

        return enrollmentMapper.toDTO(enrollment);
    }

    private double calculateProgress(Long studentId, Long courseId) {
        long totalAssessments = assessmentRepository.countByCourseId(courseId);
        if (totalAssessments == 0) {
            return 0.0;
        }

        long completedSubmissions = submissionRepository.countByStudentIdAndAssessment_Course_Id(studentId, courseId);
        return (double) completedSubmissions / totalAssessments * 100.0;
    }

    private Enrollment findEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + id));
    }
    @Override
    public List<EnrollmentDTO> getEnrollmentsByStudent(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollments.stream().map(enrollmentMapper::toDTO).collect(Collectors.toList());
    }
}