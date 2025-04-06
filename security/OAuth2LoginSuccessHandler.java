package com.example.lms.security;

import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import com.example.lms.student.Student;
import com.example.lms.student.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final StudentService studentService;

    public OAuth2LoginSuccessHandler(UserRepository userRepository, JwtUtil jwtUtil, StudentService studentService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.studentService = studentService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        List<User> existingUsers = userRepository.findAllByEmail(email);

        User user;

        if (existingUsers.isEmpty()) {
            User newUser = new User();
            newUser.setUsername(email);
            newUser.setEmail(email);
            newUser.setFullName(name);
            newUser.setPhotoUrl(picture);
            newUser.setRole("STUDENT");
            user = userRepository.save(newUser);
        } else {
            Optional<User> studentUser = existingUsers.stream()
                    .filter(u -> "STUDENT".equalsIgnoreCase(u.getRole()))
                    .findFirst();

            if (studentUser.isPresent()) {
                user = studentUser.get();
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Email is already registered with a non-student role. Please contact support.");
                return;
            }
        }

        if ("STUDENT".equalsIgnoreCase(user.getRole()) && studentService.findByUser(user) == null) {
            Student student = new Student();
            student.setMajor("Undeclared");
            studentService.createStudent(student, user);
        }

        String jwtToken = jwtUtil.generateTokenWithClaims(user);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(response.getWriter(), Collections.singletonMap("token", jwtToken));
    }
}
