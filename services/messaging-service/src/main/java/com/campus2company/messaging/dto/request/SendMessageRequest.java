package com.campus2company.messaging.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class SendMessageRequest {

    private UUID conversationId;

    @NotNull(message = "Recipient ID is required")
    private UUID recipientId;

    @NotNull(message = "Project ID is required")
    private UUID projectId;

    @NotBlank(message = "Message content is required")
    @Size(max = 5000, message = "Message must not exceed 5000 characters")
    private String content;
}
