package com.example.lms.donor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.donation.Donation;
import com.example.lms.donation.DonationDTO;
import com.example.lms.donation.DonationRepository;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import com.example.lms.donation.DonationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonorServiceImpl implements DonorService {

    private final DonorRepository donorRepository;
    private final UserRepository userRepository;
    private final DonationMapper donationMapper;
    private final DonationRepository donationRepository;

    public DonorServiceImpl(DonorRepository donorRepository, UserRepository userRepository,
            DonationRepository donationRepository, DonationMapper donationMapper) {
        this.donorRepository = donorRepository;
        this.userRepository = userRepository;
        this.donationRepository = donationRepository;
        this.donationMapper = donationMapper;
    }

    @Override
    public List<Donor> getAllDonors() {
        return donorRepository.findAll().stream()
                .filter(donor -> donor.getUser() != null)
                .collect(Collectors.toList());
    }

    @Override
    public Donor getDonorById(Long id) {
        return donorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found"));
    }

    @Override
    @Transactional
    public Donor createDonor(Donor donor, User user) {
        donor.setUser(user);
        return donorRepository.save(donor);
    }

    @Override
    @Transactional
    public Donor updateDonor(Long id, Donor updatedDonor) {
        Donor existingDonor = getDonorById(id);
        return donorRepository.save(existingDonor);
    }

    @Override
    @Transactional
    public void deleteDonor(Long id) {
        donorRepository.deleteById(id);
    }

    public Donor getDonorByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return donorRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Donor not found for user ID: " + user.getId()));
    }

    @Override
    public void deleteDuplicateDonorsForCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        List<Donor> all = donorRepository.findAll().stream()
                .filter(d -> d.getUser() != null && d.getUser().getId().equals(user.getId()))
                .sorted((a, b) -> a.getId().compareTo(b.getId()))
                .toList();

        if (all.size() <= 1)
            return;

        for (int i = 1; i < all.size(); i++) {
            donorRepository.deleteById(all.get(i).getId());
        }
    }

    @Override
    public List<DonationDTO> getDonationsByDonorId(Long donorId) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        List<Donation> donations = donationRepository.findByDonor(donor);

        return donations.stream()
                .map(donationMapper::toDTO)
                .collect(Collectors.toList());
    }

}
