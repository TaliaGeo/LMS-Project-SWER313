package com.example.lms.donation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DonationMapper {
    DonationMapper INSTANCE = Mappers.getMapper(DonationMapper.class);

    @Mapping(source = "donor.id", target = "donorId")
    @Mapping(source = "scholarship.id", target = "scholarshipId")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "donationDate", target = "date")
    DonationDTO toDTO(Donation entity);

    @Mapping(source = "donorId", target = "donor.id")
    @Mapping(source = "scholarshipId", target = "scholarship.id")
    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "date", target = "donationDate")
    Donation toEntity(DonationDTO dto);
}