package com.campus2company.admin.dto;

import com.campus2company.admin.model.ProjectLecturerAssignment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectLecturerAssignmentResponse {

    private UUID id;
    private UUID projectId;
    private UUID lecturerAuthUserId;
    private Instant assignedAt;
    private UUID assignedByAdminId;

    public static ProjectLecturerAssignmentResponse from(ProjectLecturerAssignment entity) {
        return ProjectLecturerAssignmentResponse.builder()
                .id(entity.getId())
                .projectId(entity.getProjectId())
                .lecturerAuthUserId(entity.getLecturerAuthUserId())
                .assignedAt(entity.getAssignedAt())
                .assignedByAdminId(entity.getAssignedByAdminId())
                .build();
    }
}
