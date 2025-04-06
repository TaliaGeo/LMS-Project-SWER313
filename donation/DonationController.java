package com.example.lms.donation;

import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PreAuthorize("hasRole('DONOR') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<DonationDTO>> createDonation(@RequestBody DonationDTO dto) {
        DonationDTO createdDonation = donationService.createDonation(dto);
        EntityModel<DonationDTO> donationModel = EntityModel.of(createdDonation);

        donationModel.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DonationController.class).getDonationById(createdDonation.getId()))
                .withSelfRel());

        return ResponseEntity.ok(donationModel);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DONOR')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<DonationDTO>> getDonationById(@PathVariable Long id) {
        DonationDTO donation = donationService.getDonationById(id);
        EntityModel<DonationDTO> donationModel = EntityModel.of(donation);

        donationModel.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DonationController.class).getDonationById(id)).withSelfRel());

        return ResponseEntity.ok(donationModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EntityModel<DonationDTO>>> getAllDonations() {
        List<DonationDTO> donations = donationService.getAllDonations();
        List<EntityModel<DonationDTO>> donationModels = donations.stream().map(donation -> {
            EntityModel<DonationDTO> donationModel = EntityModel.of(donation);

            donationModel.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(DonationController.class).getDonationById(donation.getId()))
                    .withSelfRel());
            return donationModel;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(donationModels);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<EntityModel<DonationDTO>>> getDonationsByDonor(@PathVariable Long donorId) {
        List<DonationDTO> donations = donationService.getDonationsByDonor(donorId);
        List<EntityModel<DonationDTO>> donationModels = donations.stream().map(donation -> {
            EntityModel<DonationDTO> donationModel = EntityModel.of(donation);

            donationModel.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(DonationController.class).getDonationById(donation.getId()))
                    .withSelfRel());
            return donationModel;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(donationModels);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        donationService.deleteDonation(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/scholarship/{scholarshipId}")
    public ResponseEntity<List<EntityModel<DonationDTO>>> getDonationsByScholarship(@PathVariable Long scholarshipId) {
        List<DonationDTO> donations = donationService.getDonationsByScholarship(scholarshipId);
        List<EntityModel<DonationDTO>> donationModels = donations.stream()
                .map(donation -> {
                    EntityModel<DonationDTO> model = EntityModel.of(donation);
                    model.add(
                            linkTo(methodOn(DonationController.class).getDonationById(donation.getId())).withSelfRel());
                    return model;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(donationModels);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EntityModel<DonationDTO>>> getDonationsByCourse(@PathVariable Long courseId) {
        List<DonationDTO> donations = donationService.getDonationsByCourse(courseId);
        List<EntityModel<DonationDTO>> donationModels = donations.stream()
                .map(d -> {
                    EntityModel<DonationDTO> model = EntityModel.of(d);
                    model.add(linkTo(methodOn(DonationController.class).getDonationById(d.getId())).withSelfRel());
                    return model;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(donationModels);
    }

}