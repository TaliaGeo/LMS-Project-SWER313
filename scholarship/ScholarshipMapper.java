package com.example.lms.scholarship;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScholarshipMapper {
    ScholarshipMapper INSTANCE = Mappers.getMapper(ScholarshipMapper.class);

    ScholarshipDTO toDTO(Scholarship entity);
    Scholarship toEntity(ScholarshipDTO dto);
}