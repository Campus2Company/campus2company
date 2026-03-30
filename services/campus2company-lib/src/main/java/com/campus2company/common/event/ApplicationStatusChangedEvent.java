package com.campus2company.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApplicationStatusChangedEvent extends BaseEvent {

    private UUID applicationId;
    private UUID studentId;
    private UUID employerId;
    private UUID projectId;
    private String projectTitle;
    private String previousStatus;
    private String newStatus;
}
