package com.campus2company.universityadmin.dto.response;

import com.campus2company.universityadmin.model.LecturerProfile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class LecturerProfileResponse {

    private UUID id;
    private UUID userId;
    private String name;
    private String email;
    private String department;
    private String facultyArea;
    private UUID universityProfileId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LecturerProfileResponse from(LecturerProfile profile) {
        return LecturerProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .name(profile.getName())
                .email(profile.getEmail())
                .department(profile.getDepartment())
                .facultyArea(profile.getFacultyArea())
                .universityProfileId(profile.getUniversityProfileId())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
