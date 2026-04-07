package com.campus2company.universityadmin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class CreateUniversityProfileRequest {

    @NotNull(message = "University ID is required")
    private UUID universityId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Domain is required")
    private String domain;

    private String description;

    private String country;
}
