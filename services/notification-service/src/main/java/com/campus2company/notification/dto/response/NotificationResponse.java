package com.campus2company.notification.dto.response;

import com.campus2company.notification.model.Notification;
import com.campus2company.notification.model.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationResponse {

    private UUID id;
    private NotificationType type;
    private String title;
    private String message;
    private UUID referenceId;
    private Boolean read;
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .referenceId(notification.getReferenceId())
                .read(notification.getRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
