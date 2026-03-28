package com.campus2company.notification.controller;

import com.campus2company.common.security.UserPrincipal;
import com.campus2company.notification.dto.response.NotificationResponse;
import com.campus2company.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Notifications", description = "User notification management")
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    @Operation(summary = "Get all notifications", description = "Returns all notifications for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "List of notifications")
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(service.getNotifications(principal.getId()));
    }

    @GetMapping("/unread")
    @Operation(summary = "Get unread notifications", description = "Returns only unread notifications.")
    @ApiResponse(responseCode = "200", description = "List of unread notifications")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(service.getUnreadNotifications(principal.getId()));
    }

    @GetMapping("/unread/count")
    @Operation(summary = "Get unread count", description = "Returns the number of unread notifications.")
    @ApiResponse(responseCode = "200", description = "Unread notification count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @AuthenticationPrincipal UserPrincipal principal) {
        long count = service.getUnreadCount(principal.getId());
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    @PutMapping("/{notificationId}/read")
    @Operation(summary = "Mark notification as read", description = "Marks a single notification as read.")
    @ApiResponse(responseCode = "200", description = "Notification marked as read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID notificationId) {
        return ResponseEntity.ok(service.markAsRead(principal.getId(), notificationId));
    }

    @PutMapping("/read-all")
    @Operation(summary = "Mark all as read", description = "Marks all notifications as read for the authenticated user.")
    @ApiResponse(responseCode = "204", description = "All notifications marked as read")
    public ResponseEntity<Void> markAllAsRead(
            @AuthenticationPrincipal UserPrincipal principal) {
        service.markAllAsRead(principal.getId());
        return ResponseEntity.noContent().build();
    }
}
