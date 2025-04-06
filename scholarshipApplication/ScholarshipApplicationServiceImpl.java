package com.example.lms.scholarshipApplication;

import com.example.lms.course.Course;
import com.example.lms.course.CourseRepository;
import com.example.lms.scholarship.Scholarship;
import com.example.lms.scholarship.ScholarshipRepository;
import com.example.lms.student.Student;
import com.example.lms.student.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScholarshipApplicationServiceImpl implements ScholarshipApplicationService {

    private final ScholarshipApplicationRepository scholarshipRepo;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ScholarshipRepository scholarshipRepository;
    private final ScholarshipApplicationMapper mapper = ScholarshipApplicationMapper.INSTANCE;

    public ScholarshipApplicationServiceImpl(
            ScholarshipApplicationRepository scholarshipRepo,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            ScholarshipRepository scholarshipRepository) {
        this.scholarshipRepo = scholarshipRepo;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.scholarshipRepository = scholarshipRepository;
    }

    @Override
    public ScholarshipApplicationDTO createApplication(ScholarshipApplicationDTO dto) {
        ScholarshipApplication entity = mapper.toEntity(dto);

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found: " + dto.getStudentId()));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found: " + dto.getCourseId()));
        Scholarship scholarship = scholarshipRepository.findById(dto.getScholarshipId())
                .orElseThrow(() -> new RuntimeException("Scholarship not found: " + dto.getScholarshipId()));

        entity.setStudent(student);
        entity.setCourse(course);
        entity.setScholarship(scholarship);

        ScholarshipApplication saved = scholarshipRepo.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public ScholarshipApplicationDTO getApplicationById(Long id) {
        ScholarshipApplication entity = scholarshipRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Scholarship application not found: " + id));
        return mapper.toDTO(entity);
    }

    @Override
    public List<ScholarshipApplicationDTO> getAllApplications() {
        return scholarshipRepo.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ScholarshipApplicationDTO updateApplication(Long id, ScholarshipApplicationDTO dto) {
        ScholarshipApplication existing = scholarshipRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Scholarship application not found: " + id));

        if (!existing.getStudent().getId().equals(dto.getStudentId())) {
            Student student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found: " + dto.getStudentId()));
            existing.setStudent(student);
        }

        if (!existing.getCourse().getId().equals(dto.getCourseId())) {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found: " + dto.getCourseId()));
            existing.setCourse(course);
        }

        if (!existing.getScholarship().getId().equals(dto.getScholarshipId())) {
            Scholarship scholarship = scholarshipRepository.findById(dto.getScholarshipId())
                    .orElseThrow(() -> new RuntimeException("Scholarship not found: " + dto.getScholarshipId()));
            existing.setScholarship(scholarship);
        }

        existing.setStatus(dto.getStatus());
        existing.setAmount(dto.getAmount());

        ScholarshipApplication updated = scholarshipRepo.save(existing);
        return mapper.toDTO(updated);
    }

    @Override
    public void deleteApplication(Long id) {
        if (!scholarshipRepo.existsById(id)) {
            throw new RuntimeException("Scholarship application not found: " + id);
        }
        scholarshipRepo.deleteById(id);
    }
}
