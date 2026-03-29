package com.campus2company.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployerRequest {

    @NotNull
    private UUID authUserId;

    @NotBlank
    private String companyName;

    @Size(max = 100)
    private String industry;

    @Size(max = 2000)
    private String description;

    @Size(max = 500)
    private String websiteUrl;
}
