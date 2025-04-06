package com.example.lms.scholarshipApplication;

import lombok.Data;

@Data
public class ScholarshipApplicationDTO {
    private Long id;
    private String status;
    private Double amount;
    private Long studentId;
    private Long courseId;
    private Long scholarshipId;

}