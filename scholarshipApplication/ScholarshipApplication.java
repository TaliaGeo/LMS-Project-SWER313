package com.example.lms.scholarshipApplication;

import com.example.lms.course.Course;
import com.example.lms.student.Student;
import com.example.lms.scholarship.Scholarship;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "scholarship_applications")
@Data
public class ScholarshipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "scholarship_id")
    private Scholarship scholarship; 
}
