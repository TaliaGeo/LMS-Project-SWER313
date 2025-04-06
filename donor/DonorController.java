package com.example.lms.donor;

import com.example.lms.donation.DonationDTO;
import com.example.lms.user.UserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/donors")
@PreAuthorize("hasRole('DONOR')")
public class DonorController {

    private final DonorService donorService;
    private final DonorMapper donorMapper;

    public DonorController(DonorService donorService, DonorMapper donorMapper) {
        this.donorService = donorService;
        this.donorMapper = donorMapper;
    }

    private EntityModel<DonorDTO> toModel(Donor donor) {
        DonorDTO dto = donorMapper.toDTO(donor);
        EntityModel<DonorDTO> model = EntityModel.of(dto);

        model.add(linkTo(methodOn(DonorController.class).getMyDonorInfo()).withSelfRel());

        if (donor.getUser() != null && donor.getUser().getId() != null) {
            model.add(linkTo(methodOn(UserController.class).getUserById(donor.getUser().getId())).withRel("user"));
        }

        return model;
    }

    @GetMapping("/me")
    public ResponseEntity<EntityModel<DonorDTO>> getMyDonorInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Donor donor = donorService.getDonorByUsername(username);
        return ResponseEntity.ok(toModel(donor));
    }

    @PutMapping("/me")
    public ResponseEntity<EntityModel<DonorDTO>> updateMyDonorInfo(@RequestBody DonorDTO donorDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Donor donor = donorService.getDonorByUsername(username);
        donor.setOrganizationName((String) donorDTO.getOrganizationName()); 
        donor.setOrganizationName(donorDTO.getOrganizationName());
        Donor updated = donorService.updateDonor(donor.getId(), donor);
        return ResponseEntity.ok(toModel(updated));
    }

    @DeleteMapping("/delete-duplicates")
    public ResponseEntity<String> deleteDuplicateDonorsForCurrentUser() {
        donorService.deleteDuplicateDonorsForCurrentUser();
        return ResponseEntity.ok("✅ Duplicates removed successfully.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/donors/{id}/donations")
    public ResponseEntity<List<DonationDTO>> getDonationsByDonorId(@PathVariable Long id) {
        List<DonationDTO> donations = donorService.getDonationsByDonorId(id);
        return ResponseEntity.ok(donations);
    }
}
