package com.example.lms.student;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import com.example.lms.attendance.Attendance;
import com.example.lms.enrollment.Enrollment;
import com.example.lms.scholarshipApplication.ScholarshipApplication;
import com.example.lms.submission.Submission;
import com.example.lms.user.User;


@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    
    private String major; 

    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Attendance> attendanceRecords;

   
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Submission> submissions;

   
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<ScholarshipApplication> scholarshipApplications;
}
