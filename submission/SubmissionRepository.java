package com.example.lms.submission;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudentId(Long studentId);
    List<Submission> findByAssessmentId(Long assessmentId);
    long countByStudentIdAndAssessment_Course_Id(Long studentId, Long courseId);
}

