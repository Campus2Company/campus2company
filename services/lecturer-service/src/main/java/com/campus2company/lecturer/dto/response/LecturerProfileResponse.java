package com.campus2company.lecturer.dto.response;

import com.campus2company.lecturer.model.LecturerProfile;
import com.campus2company.lecturer.model.SupervisorStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class LecturerProfileResponse {

    private UUID id;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String department;
    private String faculty;
    private String bio;
    private String specializations;
    private Long universityId;
    private SupervisorStatus supervisorStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LecturerProfileResponse from(LecturerProfile profile) {
        return LecturerProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .department(profile.getDepartment())
                .faculty(profile.getFaculty())
                .bio(profile.getBio())
                .specializations(profile.getSpecializations())
                .universityId(profile.getUniversityId())
                .supervisorStatus(profile.getSupervisorStatus())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
