package com.example.lms.enrollment;

import com.example.lms.course.Course;
import com.example.lms.student.Student;
import com.example.lms.user.User;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "enrollments")
@Data
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status; 
    public void updateStatus() {
        this.status = StatusUtility.getStatusBasedOnProgress(this.progress);
    }

    private Double progress = 0.0; 
    

    private Boolean completed = false;


    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student; 

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    
   
    public void calculateProgress(long completedContent, long totalContent, long submittedAssessments, long totalAssessments) {
        if (totalContent + totalAssessments == 0) {
            this.progress = 0.0;
        } else {
            this.progress = ((double) (completedContent + submittedAssessments) / (totalContent + totalAssessments)) * 100.0;
        }
        this.completed = (this.progress == 100.0);
    }
    
    
}