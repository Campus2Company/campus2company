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
public class MessageSentEvent extends BaseEvent {

    private UUID senderId;
    private UUID recipientId;
    private UUID conversationId;
    private UUID projectId;
    private String messagePreview;
}
