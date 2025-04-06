package com.example.lms.scholarship;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/scholarships")
public class ScholarshipController {

    private final ScholarshipService scholarshipService;

    public ScholarshipController(ScholarshipService scholarshipService) {
        this.scholarshipService = scholarshipService;
    }

    private EntityModel<ScholarshipDTO> toModel(ScholarshipDTO dto) {
        EntityModel<ScholarshipDTO> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(ScholarshipController.class).getScholarshipById(dto.getId())).withSelfRel());
        model.add(linkTo(methodOn(ScholarshipController.class).getAllScholarships()).withRel("all-scholarships"));
        return model;
    }
 @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<ScholarshipDTO>> createScholarship(@RequestBody ScholarshipDTO dto) {
        ScholarshipDTO created = scholarshipService.createScholarship(dto);
        return ResponseEntity.ok(toModel(created));
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ScholarshipDTO>> getScholarshipById(@PathVariable Long id) {
        ScholarshipDTO scholarship = scholarshipService.getScholarshipById(id);
        return ResponseEntity.ok(toModel(scholarship));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping
    public ResponseEntity<List<EntityModel<ScholarshipDTO>>> getAllScholarships() {
        List<ScholarshipDTO> scholarships = scholarshipService.getAllScholarships();
        List<EntityModel<ScholarshipDTO>> models = scholarships.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(models);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScholarship(@PathVariable Long id) {
        scholarshipService.deleteScholarship(id);
        return ResponseEntity.noContent().build();
    }
}
