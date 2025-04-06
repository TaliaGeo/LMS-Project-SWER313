package com.example.lms.user;

import com.example.lms.admin.*;
import com.example.lms.donor.*;
import com.example.lms.instructor.*;
import com.example.lms.student.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
public class CurrentUserController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final InstructorService instructorService;
    private final InstructorMapper instructorMapper;
    private final DonorService donorService;
    private final DonorMapper donorMapper;
    private final AdminService adminService;
    private final AdminMapper adminMapper;

    public CurrentUserController(
        StudentService studentService, StudentMapper studentMapper,
        InstructorService instructorService, InstructorMapper instructorMapper,
        DonorService donorService, DonorMapper donorMapper,
        AdminService adminService, AdminMapper adminMapper
    ) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
        this.instructorService = instructorService;
        this.instructorMapper = instructorMapper;
        this.donorService = donorService;
        this.donorMapper = donorMapper;
        this.adminService = adminService;
        this.adminMapper = adminMapper;
    }

    @GetMapping("/details")
    public ResponseEntity<?> getCurrentUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"))) {
            Student student = studentService.getStudentByUsername(username);
            return ResponseEntity.ok(EntityModel.of(studentMapper.toDTO(student)));
        }

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_INSTRUCTOR"))) {
            Instructor instructor = instructorService.getInstructorByUsername(username);
            return ResponseEntity.ok(EntityModel.of(instructorMapper.toDTO(instructor)));
        }

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DONOR"))) {
            Donor donor = donorService.getDonorByUsername(username);
            return ResponseEntity.ok(EntityModel.of(donorMapper.toDTO(donor)));
        }

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            Admin admin = adminService.getAdminByUsername(username);
            return ResponseEntity.ok(EntityModel.of(adminMapper.toDTO(admin)));
        }

        return ResponseEntity.status(403).body("❌ Unauthorized role.");
    }
}
