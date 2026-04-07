package com.campus2company.universityadmin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUniversityRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Domain is required")
    private String domain;

    private String description;

    private String country;

}
