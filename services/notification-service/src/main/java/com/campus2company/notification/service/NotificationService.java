package com.campus2company.notification.service;

import com.campus2company.notification.dto.response.NotificationResponse;
import com.campus2company.notification.exception.ResourceNotFoundException;
import com.campus2company.notification.model.Notification;
import com.campus2company.notification.model.NotificationType;
import com.campus2company.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    @Transactional
    public Notification createNotification(UUID recipientId, NotificationType type,
                                           String title, String message, UUID referenceId) {
        Notification notification = Notification.builder()
                .recipientId(recipientId)
                .type(type)
                .title(title)
                .message(message)
                .referenceId(referenceId)
                .read(false)
                .build();

        Notification saved = repository.save(notification);
        log.info("Notification created for user {}: {} - {}", recipientId, type, title);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(UUID userId) {
        return repository.findByRecipientIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications(UUID userId) {
        return repository.findByRecipientIdAndReadFalseOrderByCreatedAtDesc(userId).stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(UUID userId) {
        return repository.countByRecipientIdAndReadFalse(userId);
    }

    @Transactional
    public NotificationResponse markAsRead(UUID userId, UUID notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> ResourceNotFoundException.notification(notificationId));

        if (!notification.getRecipientId().equals(userId)) {
            throw new ResourceNotFoundException("Notification not found: " + notificationId);
        }

        notification.setRead(true);
        return NotificationResponse.from(repository.save(notification));
    }

    @Transactional
    public void markAllAsRead(UUID userId) {
        int updated = repository.markAllAsRead(userId);
        log.info("Marked {} notifications as read for user {}", updated, userId);
    }
}
