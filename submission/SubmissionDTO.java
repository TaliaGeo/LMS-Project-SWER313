package com.example.lms.submission;

import lombok.Data;


@Data
public class SubmissionDTO {
    private Long id;
    private String answers;
    private Double score;
    private Long studentId;
    private Long assessmentId;
}
