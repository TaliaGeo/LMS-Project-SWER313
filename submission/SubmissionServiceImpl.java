package com.example.lms.submission;

import org.springframework.stereotype.Service;

import com.example.lms.assessment.Assessment;
import com.example.lms.assessment.AssessmentRepository;
import com.example.lms.student.Student;
import com.example.lms.student.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final StudentRepository studentRepository;         
    private final AssessmentRepository assessmentRepository;
    private final SubmissionMapper mapper = SubmissionMapper.INSTANCE;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository,
                                 StudentRepository studentRepository,
                                 AssessmentRepository assessmentRepository) {
        this.submissionRepository = submissionRepository;
        this.studentRepository = studentRepository;
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    public SubmissionDTO createSubmission(SubmissionDTO submissionDTO) {
        Submission submission = mapper.toEntity(submissionDTO);
    
        Student student = studentRepository.findById(submissionDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + submissionDTO.getStudentId()));
    
        Assessment assessment = assessmentRepository.findById(submissionDTO.getAssessmentId())
                .orElseThrow(() -> new RuntimeException("Assessment not found with ID: " + submissionDTO.getAssessmentId()));
    
        submission.setStudent(student);
        submission.setAssessment(assessment);
        submission.setAnswers(submissionDTO.getAnswers());
    
        if ("quiz".equalsIgnoreCase(assessment.getType()) && assessment.getCorrectAnswers() != null) {
            String[] correct = assessment.getCorrectAnswers().split(",");
            String[] studentAns = submission.getAnswers().split(",");
    
            int total = correct.length;
            int score = 0;
            for (int i = 0; i < total && i < studentAns.length; i++) {
                if (correct[i].trim().equalsIgnoreCase(studentAns[i].trim())) {
                    score++;
                }
            }
            submission.setScore((double) score / total * 100);
        } else {
            submission.setScore(null); 
        }
    
        Submission saved = submissionRepository.save(submission);
        return mapper.toDTO(saved);
    }
    
    @Override
    public SubmissionDTO updateSubmission(Long submissionId, SubmissionDTO submissionDTO) {
     
        Submission existing = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new RuntimeException("Submission not found with ID: " + submissionId));

        
        if (!existing.getStudent().getId().equals(submissionDTO.getStudentId())) {
            Student student = studentRepository.findById(submissionDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + submissionDTO.getStudentId()));
            existing.setStudent(student);
        }

        if (!existing.getAssessment().getId().equals(submissionDTO.getAssessmentId())) {
            Assessment assessment = assessmentRepository.findById(submissionDTO.getAssessmentId())
                .orElseThrow(() -> new RuntimeException("Assessment not found with ID: " + submissionDTO.getAssessmentId()));
            existing.setAssessment(assessment);
        }

        
        existing.setScore(submissionDTO.getScore());

        Submission updatedSubmission = submissionRepository.save(existing);
        return mapper.toDTO(updatedSubmission);
    }

    @Override
    public SubmissionDTO getSubmissionById(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new RuntimeException("Submission not found with ID: " + submissionId));
        return mapper.toDTO(submission);
    }

    @Override
    public List<SubmissionDTO> getAllSubmissions() {
        return submissionRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSubmission(Long submissionId) {
        if (!submissionRepository.existsById(submissionId)) {
            throw new RuntimeException("Submission not found with ID: " + submissionId);
        }
        submissionRepository.deleteById(submissionId);
    }

    @Override
public List<SubmissionDTO> getSubmissionsByStudentId(Long studentId) {
    List<Submission> submissions = submissionRepository.findByStudentId(studentId);
    return submissions.stream()
            .map(mapper::toDTO)
            .collect(Collectors.toList());
}

@Override
public List<SubmissionDTO> getSubmissionsByAssessmentId(Long assessmentId) {
    List<Submission> submissions = submissionRepository.findByAssessmentId(assessmentId);
    return submissions.stream()
            .map(mapper::toDTO)
            .collect(Collectors.toList());
}


}
