package com.campus2company.auth.service;

import com.campus2company.auth.dto.request.EmployerRegisterRequest;
import com.campus2company.auth.dto.request.LoginRequest;
import com.campus2company.auth.dto.request.ProvisionUniversityAdminRequest;
import com.campus2company.auth.dto.request.RegisterRequest;
import com.campus2company.auth.dto.request.StudentRegisterRequest;
import com.campus2company.auth.dto.response.LoginResponse;
import com.campus2company.auth.dto.response.UserResponse;
import com.campus2company.auth.exception.*;
import com.campus2company.auth.model.AccountStatus;
import com.campus2company.auth.model.Role;
import com.campus2company.auth.model.UserAccount;
import com.campus2company.auth.client.EmployerServiceClient;
import com.campus2company.auth.client.StudentServiceClient;
import com.campus2company.auth.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.campus2company.common.dto.request.CreateEmployerRequest;
import com.campus2company.common.dto.request.CreateStudentRequest;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final StudentServiceClient studentServiceClient;
    private final EmployerServiceClient employerServiceClient;

    /**
     * Creates a UNIVERSITY_ADMIN row keyed by {@code id} (must match platform admin-service profile).
     */
    @Transactional
    public UserResponse provisionUniversityAdmin(ProvisionUniversityAdminRequest request) {
        UUID id = request.getId();
        if (userAccountRepository.existsById(id)) {
            throw new ApiException("User with id '" + id + "' already exists", HttpStatus.CONFLICT);
        }

        String normalizedEmail = request.getEmail().toLowerCase().trim();
        if (userAccountRepository.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException(normalizedEmail);
        }

        UserAccount user = UserAccount.builder()
                .id(id)
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.UNIVERSITY_ADMIN)
                .status(AccountStatus.ACTIVE)
                .build();

        UserAccount saved = userAccountRepository.save(user);
        log.info("University admin provisioned in auth: {} ({})", saved.getEmail(), saved.getId());
        return UserResponse.from(saved);
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        // Block platform admin registration through public API
        if (request.getRole() == Role.PLATFORM_ADMIN) {
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

        String token = jwtService.generateToken(savedUser);

        try {
            switch (request.getRole()) {
                case STUDENT -> handleStudentRegistration(savedUser.getId(), request, token);
                case EMPLOYER -> handleEmployerRegistration(savedUser.getId(), request, token);
            }
        } catch (Exception e){
            log.error("Error during post-registration processing for user {}: {}", savedUser.getEmail(), e.getMessage());
             // Rollback user creation if post registration processing fails
            throw new RuntimeException("Registration failed due to internal error. Please try again.");
        }

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

    private void handleStudentRegistration(UUID authUserId, RegisterRequest request, String token) {
        try {
            StudentRegisterRequest registerRequest = (StudentRegisterRequest) request;
            CreateStudentRequest studentRequest = CreateStudentRequest.builder()
                    .authUserId(authUserId)
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .bio(registerRequest.getBio())
                    .course(registerRequest.getCourse())
                    .faculty(registerRequest.getFaculty())
                    .studyLevel(registerRequest.getStudyLevel())
                    .universityId(registerRequest.getUniversityId())
                    .build();

            studentServiceClient.createStudent(studentRequest, "Bearer " + token);
        }catch (Exception e){
            log.error("Failed to create student profile for user ID {}: {}", authUserId, e.getMessage());
            throw new RuntimeException("Failed to create student profile. Please try again.");
        }
    }


    private void handleEmployerRegistration(UUID authUserId, RegisterRequest request, String token) {
        try {
            EmployerRegisterRequest registerRequest = (EmployerRegisterRequest) request;
            CreateEmployerRequest employerRequest = CreateEmployerRequest.builder()
                    .authUserId(authUserId)
                    .companyName(registerRequest.getCompanyName())
                    .industry(registerRequest.getIndustry())
                    .description(registerRequest.getDescription())
                    .websiteUrl(registerRequest.getWebsiteUrl())
                    .build();

            employerServiceClient.createEmployer(employerRequest, "Bearer " + token);
        } catch (Exception e) {
            log.error("Failed to create employer profile for user ID {}: {}", authUserId, e.getMessage());
            throw new RuntimeException("Failed to create employer profile. Please try again.");
        }
    }

    private AccountStatus determineInitialStatus(Role role) {
        return switch (role) {
            case STUDENT, LECTURER, UNIVERSITY_ADMIN -> AccountStatus.ACTIVE;
            case EMPLOYER -> AccountStatus.PENDING_APPROVAL;
            case PLATFORM_ADMIN -> AccountStatus.ACTIVE; // Won't reach here due to validation
        };
    }
}
