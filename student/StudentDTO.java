package com.example.lms.student;

import lombok.Data;

@Data
public class StudentDTO {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String major;
}
