package com.example.lms.user;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import com.example.lms.admin.Admin;
import com.example.lms.donor.Donor;
import com.example.lms.enrollment.Enrollment;
import com.example.lms.instructor.Instructor;
import com.example.lms.notification.Notification;
import com.example.lms.student.Student;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String email;
    private String phoneNumber;
    private String region;
    private String photoUrl;
    private String password;
    private String fullName;
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Admin> admins;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Instructor> instructors;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Student> students;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Donor> donors;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public User() {}
}