package com.example.lms.donation;

import com.example.lms.course.Course;
import com.example.lms.donor.Donor;
import com.example.lms.scholarship.Scholarship;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@Data
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private String note;

    private LocalDateTime donationDate;

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;

    @ManyToOne
    @JoinColumn(name = "scholarship_id", nullable = false)
    private Scholarship scholarship;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = true)
    private Course course;

    public Object getLocalDate() {
        throw new UnsupportedOperationException("Unimplemented method 'getLocalDate'");
    }
}