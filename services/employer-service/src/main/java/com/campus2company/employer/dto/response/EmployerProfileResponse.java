package com.campus2company.employer.dto.response;

import com.campus2company.employer.model.EmployerProfile;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EmployerProfileResponse {

    private UUID id;
    private UUID userId;
    private String companyName;
    private String industry;
    private String description;
    private String websiteUrl;
    private Boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EmployerProfileResponse from(EmployerProfile profile) {
        return EmployerProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .companyName(profile.getCompanyName())
                .industry(profile.getIndustry())
                .description(profile.getDescription())
                .websiteUrl(profile.getWebsiteUrl())
                .verified(profile.getVerified())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
