package com.example.lms.scholarship;

import org.springframework.stereotype.Service;

import com.example.lms.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScholarshipServiceImpl implements ScholarshipService {

    private final ScholarshipRepository scholarshipRepo;
    private final ScholarshipMapper mapper = ScholarshipMapper.INSTANCE;

    public ScholarshipServiceImpl(ScholarshipRepository scholarshipRepo) {
        this.scholarshipRepo = scholarshipRepo;
    }

    @Override
    public ScholarshipDTO createScholarship(ScholarshipDTO dto) {
        Scholarship scholarship = mapper.toEntity(dto);
        scholarship = scholarshipRepo.save(scholarship);
        return mapper.toDTO(scholarship);
    }

    @Override
    public ScholarshipDTO getScholarshipById(Long id) {
        Scholarship scholarship = scholarshipRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scholarship not found: " + id));

        return mapper.toDTO(scholarship);
    }

    @Override
    public List<ScholarshipDTO> getAllScholarships() {
        return scholarshipRepo.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteScholarship(Long id) {
        scholarshipRepo.deleteById(id);
    }
}
