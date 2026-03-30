package com.campus2company.notification.consumer;

import com.campus2company.common.event.AccountStatusChangedEvent;
import com.campus2company.common.event.ApplicationStatusChangedEvent;
import com.campus2company.common.event.KafkaTopics;
import com.campus2company.common.event.MessageSentEvent;
import com.campus2company.notification.model.NotificationType;
import com.campus2company.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = KafkaTopics.MESSAGE_SENT, groupId = "notification-service",
            containerFactory = "messageSentListenerFactory")
    public void handleMessageSent(MessageSentEvent event) {
        log.info("Received message.sent event: sender={}, recipient={}", event.getSenderId(), event.getRecipientId());

        String preview = event.getMessagePreview();
        if (preview != null && preview.length() > 100) {
            preview = preview.substring(0, 100) + "...";
        }

        notificationService.createNotification(
                event.getRecipientId(),
                NotificationType.NEW_MESSAGE,
                "New message received",
                preview != null ? preview : "You have a new message",
                event.getConversationId()
        );
    }

    @KafkaListener(topics = KafkaTopics.ACCOUNT_STATUS_CHANGED, groupId = "notification-service",
            containerFactory = "accountStatusListenerFactory")
    public void handleAccountStatusChanged(AccountStatusChangedEvent event) {
        log.info("Received account.status.changed event: user={}, status={}", event.getUserId(), event.getNewStatus());

        NotificationType type;
        String title;
        String message;

        switch (event.getNewStatus()) {
            case "ACTIVE" -> {
                if ("PENDING_APPROVAL".equals(event.getPreviousStatus())) {
                    type = NotificationType.ACCOUNT_APPROVED;
                    title = "Account approved";
                    message = "Your " + event.getRole().toLowerCase() + " account has been approved. You can now access the platform.";
                } else {
                    type = NotificationType.ACCOUNT_ACTIVATED;
                    title = "Account reactivated";
                    message = "Your account has been reactivated.";
                }
            }
            case "SUSPENDED" -> {
                type = NotificationType.ACCOUNT_SUSPENDED;
                title = "Account suspended";
                message = "Your account has been suspended. Contact support for more information.";
            }
            default -> {
                log.warn("Unknown account status: {}", event.getNewStatus());
                return;
            }
        }

        notificationService.createNotification(
                event.getUserId(),
                type,
                title,
                message,
                null
        );
    }

    @KafkaListener(topics = KafkaTopics.APPLICATION_STATUS_CHANGED, groupId = "notification-service",
            containerFactory = "applicationStatusListenerFactory")
    public void handleApplicationStatusChanged(ApplicationStatusChangedEvent event) {
        log.info("Received application.status.changed event: application={}, status={}",
                event.getApplicationId(), event.getNewStatus());

        String projectName = event.getProjectTitle() != null ? event.getProjectTitle() : "a project";

        switch (event.getNewStatus()) {
            case "PENDING" -> {
                // Notify employer: new application received
                notificationService.createNotification(
                        event.getEmployerId(),
                        NotificationType.APPLICATION_SUBMITTED,
                        "New application received",
                        "A student has applied to " + projectName,
                        event.getApplicationId()
                );
            }
            case "ACCEPTED" -> {
                // Notify student: application accepted
                notificationService.createNotification(
                        event.getStudentId(),
                        NotificationType.APPLICATION_ACCEPTED,
                        "Application accepted",
                        "Your application to " + projectName + " has been accepted!",
                        event.getApplicationId()
                );
            }
            case "REJECTED" -> {
                // Notify student: application rejected
                notificationService.createNotification(
                        event.getStudentId(),
                        NotificationType.APPLICATION_REJECTED,
                        "Application not successful",
                        "Your application to " + projectName + " was not successful.",
                        event.getApplicationId()
                );
            }
            case "WITHDRAWN" -> {
                // Notify employer: student withdrew
                notificationService.createNotification(
                        event.getEmployerId(),
                        NotificationType.APPLICATION_WITHDRAWN,
                        "Application withdrawn",
                        "A student has withdrawn their application to " + projectName,
                        event.getApplicationId()
                );
            }
            default -> log.warn("Unknown application status: {}", event.getNewStatus());
        }
    }
}
