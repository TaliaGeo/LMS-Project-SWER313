package com.example.lms.donor;

import org.springframework.stereotype.Component;
import com.example.lms.user.UserRepository;
import com.example.lms.user.User;

@Component
public class DonorMapper {

    private final UserRepository userRepository;

    public DonorMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DonorDTO toDTO(Donor donor) {
        DonorDTO dto = new DonorDTO();
        dto.setId(donor.getId());

        if (donor.getUser() != null) {
            dto.setUserId(donor.getUser().getId());
            dto.setFullName(donor.getUser().getUsername());
            dto.setEmail(donor.getUser().getEmail());
        }

        return dto;
    }

    public Donor toEntity(DonorDTO dto) {
        Donor donor = new Donor();

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));
            donor.setUser(user);
        }

        return donor;
    }
}
