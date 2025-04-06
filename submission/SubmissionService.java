package com.example.lms.submission;
import java.util.List;

public interface SubmissionService {
    SubmissionDTO createSubmission(SubmissionDTO submissionDTO);
    SubmissionDTO updateSubmission(Long submissionId, SubmissionDTO submissionDTO);
    SubmissionDTO getSubmissionById(Long submissionId);
    List<SubmissionDTO> getAllSubmissions();
    void deleteSubmission(Long submissionId);
    List<SubmissionDTO> getSubmissionsByStudentId(Long studentId);
    List<SubmissionDTO> getSubmissionsByAssessmentId(Long assessmentId);

}