package com.example.lms.submission;

import com.example.lms.assessment.Assessment;
import com.example.lms.course.Course;
import com.example.lms.student.Student;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "submissions")
@Data

public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answers;
    private Double score; 


    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student; 

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;
    @ManyToOne
    
@JoinColumn(name = "course_id")
private Course course;

}
