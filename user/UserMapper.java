package com.example.lms.user;

public class UserMapper {

    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRegion(user.getRegion());
        dto.setPhotoUrl(user.getPhotoUrl());
        dto.setFullName(user.getFullName());
        dto.setRole(user.getRole());
        return dto;
    }

    public static User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRegion(dto.getRegion());
        user.setPhotoUrl(dto.getPhotoUrl());
        user.setFullName(dto.getFullName());
        user.setRole(dto.getRole());
        user.setPassword(dto.getPassword()); 
        return user;
    }
}
