package com.campus2company.universityadmin.dto.response;

import com.campus2company.universityadmin.model.AssignmentStatus;
import com.campus2company.universityadmin.model.SupervisorAssignment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class SupervisorAssignmentResponse {

    private UUID id;
    private UUID lecturerProfileId;
    private UUID projectId;
    private UUID studentUserId;
    private AssignmentStatus status;
    private LocalDateTime assignedAt;

    public static SupervisorAssignmentResponse from(SupervisorAssignment assignment) {
        return SupervisorAssignmentResponse.builder()
                .id(assignment.getId())
                .lecturerProfileId(assignment.getLecturerProfileId())
                .projectId(assignment.getProjectId())
                .studentUserId(assignment.getStudentUserId())
                .status(assignment.getStatus())
                .assignedAt(assignment.getAssignedAt())
                .build();
    }
}