package com.campus2company.lecturer.dto.response;

import com.campus2company.lecturer.model.Supervision;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class SupervisionResponse {

    private UUID id;
    private UUID lecturerId;
    private UUID studentId;
    private Boolean active;
    private LocalDateTime assignedAt;

    public static SupervisionResponse from(Supervision supervision) {
        return SupervisionResponse.builder()
                .id(supervision.getId())
                .lecturerId(supervision.getLecturerId())
                .studentId(supervision.getStudentId())
                .active(supervision.getActive())
                .assignedAt(supervision.getAssignedAt())
                .build();
    }
}
