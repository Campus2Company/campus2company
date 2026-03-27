package com.campus2company.messaging.dto.response;

import com.campus2company.messaging.model.Message;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MessageResponse {

    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String content;
    private Boolean read;
    private LocalDateTime sentAt;

    public static MessageResponse from(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .senderId(message.getSenderId())
                .content(message.getContent())
                .read(message.getRead())
                .sentAt(message.getSentAt())
                .build();
    }
}
