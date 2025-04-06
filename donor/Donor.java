package com.example.lms.donor;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import com.example.lms.donation.Donation;
import com.example.lms.user.User;

@Entity
@Table(name = "donors")
@Data
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String organizationName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL)
    private List<Donation> donations;
}
