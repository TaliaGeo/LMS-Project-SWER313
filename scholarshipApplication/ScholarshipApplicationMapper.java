package com.example.lms.scholarshipApplication;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ScholarshipApplicationMapper {

    ScholarshipApplicationMapper INSTANCE = Mappers.getMapper(ScholarshipApplicationMapper.class);

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "scholarship.id", target = "scholarshipId")
    ScholarshipApplicationDTO toDTO(ScholarshipApplication entity);

    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "scholarshipId", target = "scholarship.id")
    ScholarshipApplication toEntity(ScholarshipApplicationDTO dto);
}

