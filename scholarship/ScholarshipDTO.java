package com.example.lms.scholarship;

import lombok.Data;


@Data
public class ScholarshipDTO {
    private Long id;
    private String name;
    private Double totalAmount;
    private Integer availableSlots;
    private String targetRegion;
}