package com.campus2company.universityadmin.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AssignLecturerRequest {

    @NotNull(message = "Lecturer profile ID is required")
    private UUID lecturerProfileId;

    @NotNull(message = "Project ID is required")
    private UUID projectId;

    @NotNull(message = "Student user ID is required")
    private UUID studentUserId;
}