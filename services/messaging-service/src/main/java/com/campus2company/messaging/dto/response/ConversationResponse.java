package com.campus2company.messaging.dto.response;

import com.campus2company.messaging.model.Conversation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ConversationResponse {

    private UUID id;
    private UUID participantOneId;
    private UUID participantTwoId;
    private UUID projectId;
    private String lastMessageContent;
    private LocalDateTime lastMessageAt;
    private long unreadCount;
    private LocalDateTime createdAt;

    public static ConversationResponse from(Conversation conversation, long unreadCount) {
        return ConversationResponse.builder()
                .id(conversation.getId())
                .participantOneId(conversation.getParticipantOneId())
                .participantTwoId(conversation.getParticipantTwoId())
                .projectId(conversation.getProjectId())
                .lastMessageContent(conversation.getLastMessageContent())
                .lastMessageAt(conversation.getLastMessageAt())
                .unreadCount(unreadCount)
                .createdAt(conversation.getCreatedAt())
                .build();
    }
}
