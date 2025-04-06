package com.example.lms.security;

import com.example.lms.email.EmailService;
import com.example.lms.notification.NotificationService;
import com.example.lms.notification.NotificationType;
import com.example.lms.notification.NotificationDTO;

import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import com.example.lms.donor.Donor;
import com.example.lms.donor.DonorService;
import com.example.lms.student.Student;
import com.example.lms.student.StudentService;
import com.example.lms.instructor.Instructor;
import com.example.lms.instructor.InstructorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DonorService donorService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            if (userRepo.findByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body("❌ Username already exists!");
            }

            List<String> validRoles = List.of("STUDENT", "DONOR", "INSTRUCTOR", "ADMIN");

            String role = user.getRole();
            if (role == null || !validRoles.contains(role.toUpperCase())) {
                return ResponseEntity.badRequest()
                        .body("❌ Invalid role! Allowed roles: STUDENT, DONOR, INSTRUCTOR, ADMIN.");
            }

            user.setRole(role.toUpperCase());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            User savedUser = userRepo.save(user);

            try {
                List<User> admins = userRepo.findAll().stream()
                        .filter(u -> u.getRole() != null && u.getRole().equalsIgnoreCase("ADMIN")
                                && u.getEmail() != null)
                        .toList();

                String message = "📢 A new user just registered with role: " + savedUser.getRole()
                        + ", Username: " + savedUser.getUsername();

                for (User admin : admins) {

                    NotificationDTO notification = new NotificationDTO();
                    notification.setTitle("New User Registered");
                    notification.setMessage(message);
                    notification.setType(NotificationType.GENERAL);
                    notification.setUserId(admin.getId());
                    notificationService.createNotification(notification);

                    emailService.sendAdminNotification(admin.getEmail(), message);
                }
            } catch (Exception e) {
                System.err.println("⚠️ Failed to notify admins: " + e.getMessage());
            }

            switch (savedUser.getRole()) {
                case "INSTRUCTOR" -> {
                    Instructor instructor = new Instructor();
                    instructor.setSpecialty("Unknown");
                    instructorService.createInstructor(instructor, savedUser);
                }
                case "STUDENT" -> {
                    Student student = new Student();
                    student.setMajor("Undeclared");
                    studentService.createStudent(student, savedUser);
                }
                case "DONOR" -> {
                    Donor donor = new Donor();
                    donor.setOrganizationName("Anonymous");
                    donorService.createDonor(donor, savedUser);
                }

            }

            return ResponseEntity.ok("✅ User registered and role-bound successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            User user = userRepo.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));

            String jwtToken = jwtUtil.generateToken(userDetails, user);

            try {
                System.out.println("📨 Trying to send welcome email to: " + user.getEmail());
                if (user.getEmail() != null && !user.getEmail().isBlank()) {
                    emailService.sendWelcomeEmail(user.getEmail(), user.getUsername());
                } else {
                    System.err.println("⚠️ Email is null or empty. Skipping welcome email.");
                }
            } catch (Exception e) {
                System.err.println("❌ Failed to send welcome email: " + e.getMessage());
            }

            try {
                System.out.println("🔔 Creating login notification for userId: " + user.getId());
                if (user.getId() != null) {
                    NotificationDTO notification = new NotificationDTO();
                    notification.setStatus("SENT");

                    notification.setTitle("Login Successful");
                    notification.setMessage("Welcome back, " + user.getUsername() + "!");
                    notification.setType(NotificationType.GENERAL);
                    notification.setUserId(user.getId());
                    notificationService.createNotification(notification);
                } else {
                    System.err.println("⚠️ User ID is null. Cannot create notification.");
                }
            } catch (Exception e) {
                System.err.println("❌ Failed to create login notification: " + e.getMessage());
            }

            return ResponseEntity.ok(new AuthResponse(jwtToken));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Login failed: " + e.getMessage());
        }
    }

    static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String u) {
            this.username = u;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String p) {
            this.password = p;
        }
    }

    static class AuthResponse {
        private String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}
