package com.example.lms.scholarshipApplication;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/scholarship-applications")
public class ScholarshipApplicationController {

    private final ScholarshipApplicationService scholarshipService;

    public ScholarshipApplicationController(ScholarshipApplicationService scholarshipService) {
        this.scholarshipService = scholarshipService;
    }

    private EntityModel<ScholarshipApplicationDTO> toModel(ScholarshipApplicationDTO dto) {
        EntityModel<ScholarshipApplicationDTO> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(ScholarshipApplicationController.class).getScholarship(dto.getId())).withSelfRel());
        model.add(linkTo(methodOn(ScholarshipApplicationController.class).getAllScholarships()).withRel("all-scholarship-applications"));
        return model;
    }

     @PreAuthorize("hasRole('STUDENT')or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<ScholarshipApplicationDTO>> createScholarship(@RequestBody ScholarshipApplicationDTO dto) {
        ScholarshipApplicationDTO created = scholarshipService.createApplication(dto);
        return ResponseEntity.ok(toModel(created));
    }
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ScholarshipApplicationDTO>> getScholarship(@PathVariable Long id) {
        ScholarshipApplicationDTO scholarship = scholarshipService.getApplicationById(id);
        return ResponseEntity.ok(toModel(scholarship));
    }
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EntityModel<ScholarshipApplicationDTO>>> getAllScholarships() {
        List<ScholarshipApplicationDTO> list = scholarshipService.getAllApplications();
        List<EntityModel<ScholarshipApplicationDTO>> models = list.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(models);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ScholarshipApplicationDTO>> updateScholarship(
            @PathVariable Long id,
            @RequestBody ScholarshipApplicationDTO dto) {
        ScholarshipApplicationDTO updated = scholarshipService.updateApplication(id, dto);
        return ResponseEntity.ok(toModel(updated));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScholarship(@PathVariable Long id) {
        scholarshipService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
