package com.example.lms.donor;

import lombok.Data;

@Data
public class DonorDTO {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String organizationName; 
}
