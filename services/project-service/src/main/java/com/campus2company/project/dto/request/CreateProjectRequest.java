package com.campus2company.project.dto.request;

import com.campus2company.common.enums.ProjectCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CreateProjectRequest {

    @NotBlank
    private String title;

    @Size(max = 2000)
    private String description;

    @NotEmpty
    private Set<ProjectCategory> categories;

    @NotNull
    private LocalDateTime applicationDeadline;

    @NotNull
    private LocalDateTime projectDeadline;
}