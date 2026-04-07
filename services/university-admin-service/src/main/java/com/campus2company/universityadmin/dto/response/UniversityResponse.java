package com.campus2company.universityadmin.dto.response;

import com.campus2company.universityadmin.model.University;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder

public class UniversityResponse {

    private UUID id;
    private String name;
    private String domain;
    private String description;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static UniversityResponse from(University university) {
        return UniversityResponse.builder()
                .id(university.getId())
                .name(university.getName())
                .domain(university.getDomain())
                .description(university.getDescription())
                .country(university.getCountry())
                .createdAt(university.getCreatedAt())
                .updatedAt(university.getUpdatedAt())
                .build();
    }
}
