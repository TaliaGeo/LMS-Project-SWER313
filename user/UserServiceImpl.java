package com.example.lms.user;

import com.example.lms.exceptions.ResourceNotFoundException;
import com.example.lms.exceptions.UsernameAlreadyExistsException;
import com.example.lms.student.Student;
import com.example.lms.student.StudentRepository;
import com.example.lms.student.StudentService;
import com.example.lms.instructor.Instructor;
import com.example.lms.instructor.InstructorService;
import com.example.lms.instructor.InstructorRepository;
import com.example.lms.donor.Donor;
import com.example.lms.donor.DonorService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final StudentService studentService;
    private final InstructorService instructorService;
    private final DonorService donorService;

    public UserServiceImpl(UserRepository userRepository, StudentService studentService, InstructorService instructorService, DonorService donorService) {
        this.userRepository = userRepository;
        this.studentService = studentService;
        this.instructorService = instructorService;
        this.donorService = donorService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (!userRepository.findAllByEmail(userDto.getEmail()).isEmpty() ||
        userRepository.findByUsername(userDto.getName()).isPresent()) {
        throw new UsernameAlreadyExistsException("User with this email or username already exists.");
    }
    
     

        User user = UserMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        switch (user.getRole().toUpperCase()) {
            case "STUDENT" -> {
                Student student = new Student();
                studentService.createStudent(student, savedUser);
            }
            case "INSTRUCTOR" -> {
                Instructor instructor = new Instructor();
                instructorService.createInstructor(instructor);
            }
            case "DONOR" -> {
                Donor donor = new Donor();
                donorService.createDonor(donor, savedUser);
            }
        }

        return UserMapper.toDto(savedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

                if (!userRepository.findAllByEmail(userDto.getEmail()).isEmpty() &&
                !user.getEmail().equals(userDto.getEmail())) {
                throw new UsernameAlreadyExistsException("Email already in use by another user.");
            }
            
        user.setUsername(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRegion(userDto.getRegion());
        user.setPhotoUrl(userDto.getPhotoUrl());
        user.setFullName(userDto.getFullName());

        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
