package com.example.lms.donor;

import com.example.lms.donation.Donation;
import com.example.lms.donation.DonationRepository;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/donors/dashboard")
@PreAuthorize("hasRole('DONOR')")
public class DonorDashboardController {

    private final DonationRepository donationRepo;
    private final DonorRepository donorRepo;
    private final UserRepository userRepo;

    public DonorDashboardController(DonationRepository donationRepo,
            DonorRepository donorRepo,
            UserRepository userRepo) {
        this.donationRepo = donationRepo;
        this.donorRepo = donorRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/stats")
    @Transactional
    public ResponseEntity<?> getDonorStats(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Donor donor = donorRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        List<Donation> donations = donationRepo.findByDonor(donor);

        double totalDonated = donations.stream()
                .mapToDouble(d -> d.getAmount() != null ? d.getAmount() : 0.0)
                .sum();

        long donationsCount = donations.size();

        String lastDonationDate = donations.stream()
                .filter(d -> d.getDonationDate() != null)
                .max(Comparator.comparing(Donation::getDonationDate))
                .map(d -> d.getDonationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .orElse("No donations yet");
        List<Map<String, Object>> topScholarships = donations.stream()
                .filter(d -> d.getScholarship() != null)
                .collect(Collectors.groupingBy(
                        d -> d.getScholarship().getName(),
                        Collectors.summingDouble(d -> d.getAmount() != null ? d.getAmount() : 0.0)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", e.getKey());
                    map.put("total", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> topCourses = donations.stream()
                .filter(d -> d.getCourse() != null)
                .collect(Collectors.groupingBy(
                        d -> d.getCourse().getTitle(),
                        Collectors.summingDouble(d -> d.getAmount() != null ? d.getAmount() : 0.0)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", e.getKey());
                    map.put("total", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("totalDonated", totalDonated);
        response.put("donationsCount", donationsCount);
        response.put("lastDonationDate", lastDonationDate);
        response.put("topScholarships", topScholarships);
        response.put("topCourses", topCourses);
        return ResponseEntity.ok(response);
    }
}
