package com.example.lms.security;

import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class TokenTestController {

    private final JwtUtil jwtUtil;

    public TokenTestController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/test-token")
    public ResponseEntity<?> testToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body("❌ Authorization header missing or invalid");
            }

            String token = authHeader.substring(7);

            Claims claims = jwtUtil.extractAllClaims(token);
            return ResponseEntity.ok(claims);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Invalid token: " + e.getMessage());
        }
    }
}
