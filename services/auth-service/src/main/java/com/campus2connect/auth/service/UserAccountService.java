package com.campus2connect.auth.service;

import com.campus2connect.auth.exception.InvalidStatusTransitionException;
import com.campus2connect.auth.exception.UserNotFoundException;
import com.campus2connect.auth.model.AccountStatus;
import com.campus2connect.auth.model.Role;
import com.campus2connect.auth.model.UserAccount;
import com.campus2connect.auth.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccount findById(UUID userId) {
        return userAccountRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public UserAccount findByEmail(String email) {
        return userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Transactional
    public UserAccount approveCompany(UUID userId) {
        UserAccount user = findById(userId);

        if (user.getRole() != Role.COMPANY || user.getStatus() != AccountStatus.PENDING_APPROVAL) {
            throw InvalidStatusTransitionException.forApproval(user.getRole(), user.getStatus());
        }

        user.setStatus(AccountStatus.ACTIVE);
        log.info("Company account approved: {}", user.getEmail());
        return userAccountRepository.save(user);
    }

    @Transactional
    public UserAccount suspendUser(UUID userId) {
        UserAccount user = findById(userId);
        user.setStatus(AccountStatus.SUSPENDED);
        log.info("User account suspended: {}", user.getEmail());
        return userAccountRepository.save(user);
    }

    @Transactional
    public UserAccount activateUser(UUID userId) {
        UserAccount user = findById(userId);
        user.setStatus(AccountStatus.ACTIVE);
        log.info("User account activated: {}", user.getEmail());
        return userAccountRepository.save(user);
    }
}
