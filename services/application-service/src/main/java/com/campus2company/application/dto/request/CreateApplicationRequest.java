package com.campus2company.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateApplicationRequest {

    @NotNull(message = "Project ID is required")
    private UUID projectId;

    @NotNull(message = "Employer ID is required")
    private UUID employerId;

    private String projectTitle;

    @Size(max = 5000, message = "Cover letter must not exceed 5000 characters")
    private String coverLetter;
}
