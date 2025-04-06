package com.example.lms.donation;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class DonationDTO {
    private Long id;
    private Double amount;
    private String note;
    private Long donorId;
    private Long courseId;
    private LocalDateTime date;
    private Long scholarshipId;

}

