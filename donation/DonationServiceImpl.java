package com.example.lms.donation;

import com.example.lms.course.Course;
import com.example.lms.course.CourseRepository;
import com.example.lms.donor.Donor;
import com.example.lms.donor.DonorRepository;
import com.example.lms.scholarship.Scholarship;
import com.example.lms.scholarship.ScholarshipRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepo;
    private final DonorRepository donorRepo;
    private final DonationMapper mapper = DonationMapper.INSTANCE;
    private final ScholarshipRepository scholarshipRepo;
    private final CourseRepository courseRepository;

    public DonationServiceImpl(DonationRepository donationRepo, DonorRepository donorRepo,
            ScholarshipRepository scholarshipRepo, CourseRepository courseRepository) {
        this.donationRepo = donationRepo;
        this.donorRepo = donorRepo;
        this.scholarshipRepo = scholarshipRepo;
        this.courseRepository = courseRepository;
    }

    @Override
    public DonationDTO getDonationById(Long id) {
        Donation donation = donationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found: " + id));
        return mapper.toDTO(donation);
    }

    @Override
    public List<DonationDTO> getAllDonations() {
        return donationRepo.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonationDTO> getDonationsByDonor(Long donorId) {
        Donor donor = donorRepo.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found: " + donorId));
        return donationRepo.findByDonor(donor).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDonation(Long id) {
        if (!donationRepo.existsById(id)) {
            throw new RuntimeException("Donation not found: " + id);
        }
        donationRepo.deleteById(id);
    }

    @Override
public DonationDTO createDonation(DonationDTO dto) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    Donor donor = donorRepo.findByUser_Username(username)
            .orElseThrow(() -> new RuntimeException("Donor not found for user: " + username));

    Scholarship scholarship = scholarshipRepo.findById(dto.getScholarshipId())
            .orElseThrow(() -> new RuntimeException("Scholarship not found: " + dto.getScholarshipId()));

    Course course = null;
    if (dto.getCourseId() != null) {
        course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found: " + dto.getCourseId()));
    }

    Donation donation = mapper.toEntity(dto);
    donation.setDonor(donor); 
    donation.setScholarship(scholarship);
    donation.setCourse(course);
    donation.setDonationDate(LocalDateTime.now());

    Donation savedDonation = donationRepo.save(donation);
    return mapper.toDTO(savedDonation);
}


    @Override
    public List<DonationDTO> getDonationsByScholarship(Long scholarshipId) {
        Scholarship scholarship = scholarshipRepo.findById(scholarshipId)
                .orElseThrow(() -> new RuntimeException("Scholarship not found: " + scholarshipId));

        List<Donation> donations = donationRepo.findByScholarship(scholarship);
        return donations.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<DonationDTO> getDonationsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));

        List<Donation> donations = donationRepo.findByCourse(course);
        return donations.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

}