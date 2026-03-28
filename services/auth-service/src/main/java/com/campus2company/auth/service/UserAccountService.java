package com.campus2company.auth.service;

import com.campus2company.auth.exception.InvalidStatusTransitionException;
import com.campus2company.auth.exception.UserNotFoundException;
import com.campus2company.auth.model.AccountStatus;
import com.campus2company.auth.model.Role;
import com.campus2company.auth.model.UserAccount;
import com.campus2company.auth.repository.UserAccountRepository;
import com.campus2company.common.event.AccountStatusChangedEvent;
import com.campus2company.common.event.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserAccount findById(UUID userId) {
        return userAccountRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public UserAccount findByEmail(String email) {
        return userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Transactional
    public UserAccount approveEmployer(UUID userId) {
        UserAccount user = findById(userId);

        if (user.getRole() != Role.EMPLOYER || user.getStatus() != AccountStatus.PENDING_APPROVAL) {
            throw InvalidStatusTransitionException.forApproval(user.getRole(), user.getStatus());
        }

        String previousStatus = user.getStatus().name();
        user.setStatus(AccountStatus.ACTIVE);
        UserAccount saved = userAccountRepository.save(user);
        log.info("Employer account approved: {}", user.getEmail());

        publishAccountStatusEvent(saved, previousStatus);
        return saved;
    }

    @Transactional
    public UserAccount suspendUser(UUID userId) {
        UserAccount user = findById(userId);
        String previousStatus = user.getStatus().name();
        user.setStatus(AccountStatus.SUSPENDED);
        UserAccount saved = userAccountRepository.save(user);
        log.info("User account suspended: {}", user.getEmail());

        publishAccountStatusEvent(saved, previousStatus);
        return saved;
    }

    @Transactional
    public UserAccount activateUser(UUID userId) {
        UserAccount user = findById(userId);
        String previousStatus = user.getStatus().name();
        user.setStatus(AccountStatus.ACTIVE);
        UserAccount saved = userAccountRepository.save(user);
        log.info("User account activated: {}", user.getEmail());

        publishAccountStatusEvent(saved, previousStatus);
        return saved;
    }

    private void publishAccountStatusEvent(UserAccount user, String previousStatus) {
        AccountStatusChangedEvent event = AccountStatusChangedEvent.builder()
                .eventId(UUID.randomUUID())
                .eventType("account.status.changed")
                .timestamp(LocalDateTime.now())
                .source("auth-service")
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .previousStatus(previousStatus)
                .newStatus(user.getStatus().name())
                .build();

        kafkaTemplate.send(KafkaTopics.ACCOUNT_STATUS_CHANGED, event);
        log.info("Published account.status.changed event for user {}", user.getId());
    }
}
