package com.example.lms.scholarship;
import java.util.List;

public interface ScholarshipService {
    ScholarshipDTO createScholarship(ScholarshipDTO dto);
    ScholarshipDTO getScholarshipById(Long id);
    List<ScholarshipDTO> getAllScholarships();
    void deleteScholarship(Long id);
}

