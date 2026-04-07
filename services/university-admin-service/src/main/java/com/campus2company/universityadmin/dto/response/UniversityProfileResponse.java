package com.campus2company.universityadmin.dto.response;

import com.campus2company.universityadmin.model.UniversityProfile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UniversityProfileResponse {

    private UUID id;
    private UUID universityId;
    private UUID userId;
    private String name;
    private String domain;
    private String description;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UniversityProfileResponse from(UniversityProfile profile) {
        return UniversityProfileResponse.builder()
                .id(profile.getId())
                .universityId(profile.getUniversityId())
                .userId(profile.getUserId())
                .name(profile.getName())
                .domain(profile.getDomain())
                .description(profile.getDescription())
                .country(profile.getCountry())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
