package com.example.lms.course;

import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private Boolean isFree;
    private Double price;
    private Long instructorId;
}