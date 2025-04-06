package com.example.lms.user;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String region;
    private String photoUrl;
    private String fullName;
    private String password;
    private String role;      
}
