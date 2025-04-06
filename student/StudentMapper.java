package com.example.lms.student;

import com.example.lms.user.User;
import com.example.lms.user.UserRepository;

import lombok.Data;

import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    private final UserRepository userRepository;

    public StudentMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setMajor(student.getMajor());

        if (student.getUser() != null) {
            dto.setUserId(student.getUser().getId());
            dto.setFullName(student.getUser().getUsername());
            dto.setEmail(student.getUser().getEmail());
            dto.setPhoneNumber(student.getUser().getPhoneNumber());
        }

        return dto;
    }

    public Student toEntity(StudentDTO dto) {
        Student student = new Student();
        student.setMajor(dto.getMajor());

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));
            student.setUser(user);
        }

        return student;
    }

    @Data
    public static class StudentDTO {
        private Long id;
        private Long userId;
        private String fullName;
        private String email;
        private String phoneNumber;
        private String major;
    }
}