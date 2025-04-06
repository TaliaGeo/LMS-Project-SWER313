package com.example.lms.donation;

import java.util.List;

import com.example.lms.scholarship.Scholarship;

public interface DonationService {
    DonationDTO createDonation(DonationDTO dto);
    DonationDTO getDonationById(Long id);
    List<DonationDTO> getAllDonations();
    List<DonationDTO> getDonationsByDonor(Long donorId);
    void deleteDonation(Long id);
    List<DonationDTO> getDonationsByScholarship(Long scholarshipId);
    List<DonationDTO> getDonationsByCourse(Long courseId);


}