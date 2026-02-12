package com.campus2connect.auth.service;

import com.campus2connect.auth.dto.request.LoginRequest;
import com.campus2connect.auth.dto.request.RegisterRequest;
import com.campus2connect.auth.dto.response.LoginResponse;
import com.campus2connect.auth.dto.response.UserResponse;
import com.campus2connect.auth.exception.*;
import com.campus2connect.auth.model.AccountStatus;
import com.campus2connect.auth.model.Role;
import com.campus2connect.auth.model.UserAccount;
import com.campus2connect.auth.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        // Block admin registration through API
        if (request.getRole() == Role.ADMIN) {
            throw new AdminRegistrationNotAllowedException();
        }

        // Check for existing user
        if (userAccountRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        // Determine initial status based on role
        AccountStatus initialStatus = determineInitialStatus(request.getRole());

        // Create user account
        UserAccount user = UserAccount.builder()
                .email(request.getEmail().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .status(initialStatus)
                .build();

        UserAccount savedUser = userAccountRepository.save(user);
        log.info("User registered: {} with role {} and status {}",
                savedUser.getEmail(), savedUser.getRole(), savedUser.getStatus());

        return UserResponse.from(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        // Find user by email
        UserAccount user = userAccountRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(InvalidCredentialsException::new);

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        // Check account status
        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(user.getStatus());
        }

        // Generate JWT
        String token = jwtService.generateToken(user);

        log.info("User logged in: {}", user.getEmail());

        return LoginResponse.of(token, jwtService.getExpirySeconds(), UserResponse.from(user));
    }

    private AccountStatus determineInitialStatus(Role role) {
        return switch (role) {
            case STUDENT, UNIVERSITY -> AccountStatus.ACTIVE;
            case COMPANY -> AccountStatus.PENDING_APPROVAL;
            case ADMIN -> AccountStatus.ACTIVE; // Won't reach here due to validation
        };
    }
}
