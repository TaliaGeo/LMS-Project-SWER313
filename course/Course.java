package com.example.lms.course;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import com.example.lms.assessment.Assessment;
import com.example.lms.attendance.Attendance;
import com.example.lms.content.Content;
import com.example.lms.donation.Donation;
import com.example.lms.enrollment.Enrollment;
import com.example.lms.instructor.Instructor;


@Entity
@Table(name = "courses")
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    
    private Boolean isFree; 
    private Double price;   

  
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Content> contents;

   
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Assessment> assessments;

    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Attendance> attendanceRecords;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Donation> donations;

}