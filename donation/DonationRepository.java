package com.example.lms.donation;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lms.course.Course;
import com.example.lms.donor.Donor;
import com.example.lms.scholarship.Scholarship;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByDonor(Donor donor);
     List<Donation> findByScholarship(Scholarship scholarship);
List<Donation> findByCourse(Course course);
}
