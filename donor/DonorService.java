package com.example.lms.donor;

import java.util.List;

import com.example.lms.donation.DonationDTO;
import com.example.lms.user.User;

public interface DonorService {
    List<Donor> getAllDonors();
    Donor getDonorById(Long id);
    Donor getDonorByUsername(String username); 
    Donor createDonor(Donor donor, User user);
    Donor updateDonor(Long id, Donor updatedDonor);
    void deleteDonor(Long id);
    void deleteDuplicateDonorsForCurrentUser();
    List<DonationDTO> getDonationsByDonorId(Long donorId);
    
}
