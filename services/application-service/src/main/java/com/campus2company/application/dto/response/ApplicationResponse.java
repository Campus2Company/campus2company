package com.campus2company.application.dto.response;

import com.campus2company.application.model.Application;
import com.campus2company.common.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ApplicationResponse {

    private UUID id;
    private UUID studentId;
    private UUID projectId;
    private UUID employerId;
    private String projectTitle;
    private ApplicationStatus status;
    private String coverLetter;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    public static ApplicationResponse from(Application app) {
        return ApplicationResponse.builder()
                .id(app.getId())
                .studentId(app.getStudentId())
                .projectId(app.getProjectId())
                .employerId(app.getEmployerId())
                .projectTitle(app.getProjectTitle())
                .status(app.getStatus())
                .coverLetter(app.getCoverLetter())
                .appliedAt(app.getAppliedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }
}
