package com.campus2company.messaging.service;

import com.campus2company.messaging.dto.request.SendMessageRequest;
import com.campus2company.messaging.dto.response.ConversationResponse;
import com.campus2company.messaging.dto.response.MessageResponse;
import com.campus2company.messaging.exception.ForbiddenException;
import com.campus2company.messaging.exception.ResourceNotFoundException;
import com.campus2company.messaging.model.Conversation;
import com.campus2company.messaging.model.Message;
import com.campus2company.messaging.repository.ConversationRepository;
import com.campus2company.messaging.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public MessageResponse sendMessage(UUID senderId, SendMessageRequest request) {
        if (senderId.equals(request.getRecipientId())) {
            throw new ForbiddenException("Cannot send a message to yourself");
        }

        Conversation conversation;

        // If conversationId provided, use existing conversation
        if (request.getConversationId() != null) {
            conversation = conversationRepository.findById(request.getConversationId())
                    .orElseThrow(() -> ResourceNotFoundException.conversation(request.getConversationId()));
            verifyParticipant(conversation, senderId);
        } else {
            // Find or create conversation for this project
            conversation = conversationRepository
                    .findByParticipantsAndProject(senderId, request.getRecipientId(), request.getProjectId())
                    .orElseGet(() -> {
                        Conversation newConversation = Conversation.builder()
                                .participantOneId(senderId)
                                .participantTwoId(request.getRecipientId())
                                .projectId(request.getProjectId())
                                .build();
                        log.info("Created new conversation between {} and {} for project {}",
                                senderId, request.getRecipientId(), request.getProjectId());
                        return conversationRepository.save(newConversation);
                    });
        }

        // Create message
        Message message = Message.builder()
                .conversation(conversation)
                .senderId(senderId)
                .content(request.getContent())
                .read(false)
                .build();

        Message saved = messageRepository.save(message);

        // Update conversation with last message info
        conversation.setLastMessageContent(request.getContent());
        conversation.setLastMessageAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        log.info("Message sent from {} in conversation {}", senderId, conversation.getId());
        return MessageResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> getConversations(UUID userId) {
        List<Conversation> conversations = conversationRepository.findAllByParticipant(userId);
        return conversations.stream()
                .map(c -> {
                    long unread = messageRepository.countUnreadInConversation(c.getId(), userId);
                    return ConversationResponse.from(c, unread);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessages(UUID userId, UUID conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> ResourceNotFoundException.conversation(conversationId));

        verifyParticipant(conversation, userId);

        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId).stream()
                .map(MessageResponse::from)
                .toList();
    }

    @Transactional
    public void markConversationAsRead(UUID userId, UUID conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> ResourceNotFoundException.conversation(conversationId));

        verifyParticipant(conversation, userId);

        int updated = messageRepository.markAllAsRead(conversationId, userId);
        log.info("Marked {} messages as read for user {} in conversation {}", updated, userId, conversationId);
    }

    private void verifyParticipant(Conversation conversation, UUID userId) {
        if (!conversation.getParticipantOneId().equals(userId)
                && !conversation.getParticipantTwoId().equals(userId)) {
            throw new ForbiddenException("You are not a participant in this conversation");
        }
    }
}
