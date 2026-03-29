package com.campus2company.project.dto.response;

import com.campus2company.common.enums.ProjectCategory;
import com.campus2company.common.enums.ProjectStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "employerId",
        "categories",
        "status",
        "applicationDeadline",
        "projectDeadline",
        "createdAt",
        "updatedAt"
})
@Data
@Builder
public class ProjectResponse {
    private UUID id;

    private String title;

    private String description;

    private UUID employerId;

    private Set<ProjectCategory> categories;

    private ProjectStatus status;

    private LocalDateTime applicationDeadline;

    private LocalDateTime projectDeadline;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}