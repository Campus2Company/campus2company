package com.campus2company.project.dto.request;

import com.campus2company.common.enums.ProjectCategory;
import com.campus2company.common.enums.ProjectStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UpdateProjectRequest {

    private String title;

    @Size(max = 2000)
    private String description;


    private Set<ProjectCategory> categories;

    private ProjectStatus status;


    private LocalDateTime applicationDeadline;


    private LocalDateTime projectDeadline;
}
