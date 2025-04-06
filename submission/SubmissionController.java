package com.example.lms.submission;

import com.example.lms.admin.AdminController;
import com.example.lms.assessment.AssessmentController;
import com.example.lms.student.StudentController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    private EntityModel<SubmissionDTO> toModel(SubmissionDTO submissionDTO) {
        EntityModel<SubmissionDTO> model = EntityModel.of(submissionDTO);
        model.add(linkTo(methodOn(SubmissionController.class).getSubmissionById(submissionDTO.getId())).withSelfRel());
        model.add(linkTo(methodOn(SubmissionController.class).getAllSubmissions()).withRel("all-submissions"));

        if (submissionDTO.getStudentId() != null) {
            model.add(linkTo(methodOn(AdminController.class).getStudentById(submissionDTO.getStudentId()))
                    .withRel("student"));
        }

        if (submissionDTO.getAssessmentId() != null) {
            model.add(linkTo(methodOn(AssessmentController.class).getAssessmentById(submissionDTO.getAssessmentId()))
                    .withRel("assessment"));
        }

        return model;
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping
    public ResponseEntity<List<EntityModel<SubmissionDTO>>> getAllSubmissions() {
        List<SubmissionDTO> submissions = submissionService.getAllSubmissions();
        List<EntityModel<SubmissionDTO>> models = submissions.stream().map(this::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(models);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SubmissionDTO>> getSubmissionById(@PathVariable Long id) {
        SubmissionDTO submissionDTO = submissionService.getSubmissionById(id);
        return ResponseEntity.ok(toModel(submissionDTO));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<EntityModel<SubmissionDTO>> createSubmission(@RequestBody SubmissionDTO submissionDTO) {
        SubmissionDTO created = submissionService.createSubmission(submissionDTO);
        return ResponseEntity.ok(toModel(created));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SubmissionDTO>> updateSubmission(
        @PathVariable Long id,
        @RequestBody SubmissionDTO submissionDTO
) {
    SubmissionDTO updated = submissionService.updateSubmission(id, submissionDTO);
    return ResponseEntity.ok(toModel(updated));
}


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EntityModel<SubmissionDTO>>> getSubmissionsByStudentId(
            @PathVariable Long studentId
    ) {
        List<SubmissionDTO> submissions = submissionService.getSubmissionsByStudentId(studentId);
        List<EntityModel<SubmissionDTO>> models = submissions.stream().map(this::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(models);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/assessment/{assessmentId}")
    public ResponseEntity<List<EntityModel<SubmissionDTO>>> getSubmissionsByAssessmentId(
            @PathVariable Long assessmentId
    ) {
        List<SubmissionDTO> submissions = submissionService.getSubmissionsByAssessmentId(assessmentId);
        List<EntityModel<SubmissionDTO>> models = submissions.stream().map(this::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(models);
    }
}
