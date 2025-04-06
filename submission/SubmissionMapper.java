package com.example.lms.submission;

import com.example.lms.submission.SubmissionDTO;
import com.example.lms.submission.Submission;


import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubmissionMapper {
    SubmissionMapper INSTANCE = Mappers.getMapper(SubmissionMapper.class);

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "assessment.id", target = "assessmentId")
    SubmissionDTO toDTO(Submission submission);

    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "assessmentId", target = "assessment.id")
    Submission toEntity(SubmissionDTO dto);
}
