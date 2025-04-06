package com.example.lms.scholarship;

import jakarta.persistence.*;
import lombok.Data;

import com.example.lms.donation.Donation;
import com.example.lms.scholarshipApplication.ScholarshipApplication;
import java.util.List;

@Entity
@Table(name = "scholarships")
@Data
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  
    private Double totalAmount;  
    private Integer availableSlots;
    private String targetRegion;  

    @OneToMany(mappedBy = "scholarship", cascade = CascadeType.ALL)
    private List<ScholarshipApplication> applications;  

     @OneToMany(mappedBy = "scholarship", cascade = CascadeType.ALL)
    private List<Donation> donations;
}