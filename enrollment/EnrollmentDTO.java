
package com.example.lms.enrollment;

import lombok.Data;

@Data
public class EnrollmentDTO {
    private Long id;
    private String status;
    private Double progress;
    private Boolean completed;
    private Long studentId;
    private Long courseId;
    private Long userId;
}
