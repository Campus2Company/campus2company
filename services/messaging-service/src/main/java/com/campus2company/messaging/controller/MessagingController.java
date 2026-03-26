package com.campus2company.messaging.controller;

import com.campus2company.common.security.UserPrincipal;
import com.campus2company.messaging.dto.request.SendMessageRequest;
import com.campus2company.messaging.dto.response.ConversationResponse;
import com.campus2company.messaging.dto.response.MessageResponse;
import com.campus2company.messaging.service.MessagingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Messaging", description = "Direct messaging between users")
public class MessagingController {

    private final MessagingService service;

    @PostMapping
    @Operation(summary = "Send a message",
            description = "Send a message to another user. Creates a conversation if one doesn't exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Message sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Cannot message yourself")
    })
    public ResponseEntity<MessageResponse> sendMessage(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody SendMessageRequest request) {
        MessageResponse response = service.sendMessage(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/conversations")
    @Operation(summary = "Get my conversations",
            description = "Returns all conversations for the authenticated user, ordered by most recent message.")
    @ApiResponse(responseCode = "200", description = "List of conversations")
    public ResponseEntity<List<ConversationResponse>> getConversations(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(service.getConversations(principal.getId()));
    }

    @GetMapping("/conversations/{conversationId}")
    @Operation(summary = "Get messages in a conversation",
            description = "Returns all messages in a conversation, ordered by time sent.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of messages"),
            @ApiResponse(responseCode = "403", description = "Not a participant in this conversation"),
            @ApiResponse(responseCode = "404", description = "Conversation not found")
    })
    public ResponseEntity<List<MessageResponse>> getMessages(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID conversationId) {
        return ResponseEntity.ok(service.getMessages(principal.getId(), conversationId));
    }

    @PutMapping("/conversations/{conversationId}/read")
    @Operation(summary = "Mark conversation as read",
            description = "Marks all unread messages from the other participant as read.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Messages marked as read"),
            @ApiResponse(responseCode = "403", description = "Not a participant in this conversation"),
            @ApiResponse(responseCode = "404", description = "Conversation not found")
    })
    public ResponseEntity<Void> markAsRead(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID conversationId) {
        service.markConversationAsRead(principal.getId(), conversationId);
        return ResponseEntity.noContent().build();
    }
}
