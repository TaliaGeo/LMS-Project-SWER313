package com.example.lms.scholarshipApplication;


import java.util.List;

public interface ScholarshipApplicationService {
    ScholarshipApplicationDTO createApplication(ScholarshipApplicationDTO dto);
    ScholarshipApplicationDTO getApplicationById(Long id);
    List<ScholarshipApplicationDTO> getAllApplications();
    ScholarshipApplicationDTO updateApplication(Long id, ScholarshipApplicationDTO dto);
    void deleteApplication(Long id);
}