package com.example.lms.course;

import com.example.lms.instructor.Instructor;
import com.example.lms.instructor.InstructorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CourseMapper {

    private final InstructorRepository instructorRepository;

    public CourseMapper(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public CourseDTO toDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setIsFree(course.getIsFree());
        dto.setPrice(course.getPrice());

        Optional.ofNullable(course.getInstructor())
                .map(Instructor::getId)
                .ifPresent(dto::setInstructorId);

        return dto;
    }

    public Course toEntity(CourseDTO dto) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setIsFree(dto.getIsFree());
        course.setPrice(dto.getPrice());

        Optional.ofNullable(dto.getInstructorId())
                .flatMap(instructorRepository::findById)
                .ifPresent(course::setInstructor);

        return course;
    }

    public void updateEntity(Course course, CourseDTO dto) {
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setIsFree(dto.getIsFree());
        course.setPrice(dto.getPrice());

        Optional.ofNullable(dto.getInstructorId())
                .flatMap(instructorRepository::findById)
                .ifPresent(course::setInstructor);
    }
}
