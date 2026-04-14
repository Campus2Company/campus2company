package com.campus2company.admin.service;

import com.campus2company.admin.client.AuthPlatformClient;
import com.campus2company.admin.dto.AuthUserResponse;
import com.campus2company.admin.dto.CreatePlatformAdminRequest;
import com.campus2company.admin.dto.PlatformAdminResponse;
import com.campus2company.admin.dto.ProvisionPlatformAdminAuthRequest;
import com.campus2company.admin.exception.ApiException;
import com.campus2company.admin.model.PlatformAdminProfile;
import com.campus2company.admin.repository.PlatformAdminProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminOperationsService {

    private final AuthPlatformClient authPlatformClient;
    private final PlatformAdminProfileRepository platformAdminProfileRepository;

    @Transactional
    public PlatformAdminResponse createPlatformAdmin(
            CreatePlatformAdminRequest request,
            String authorizationHeader) {

        String normalizedEmail = request.getEmail().toLowerCase().trim();
        if (platformAdminProfileRepository.existsByEmail(normalizedEmail)) {
            throw new ApiException("A university admin with this email already exists", HttpStatus.CONFLICT);
        }

        UUID id = UUID.randomUUID();
        PlatformAdminProfile profile = PlatformAdminProfile.builder()
                .id(id)
                .email(normalizedEmail)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .universityId(request.getUniversityId())
                .createdAt(Instant.now())
                .build();

        try {
            // Flush now so unique-email violations are raised before auth provisioning.
            platformAdminProfileRepository.saveAndFlush(profile);
        } catch (DataIntegrityViolationException ex) {
            throw new ApiException("A university admin with this email already exists", HttpStatus.CONFLICT);
        }

        try {
            ProvisionPlatformAdminAuthRequest authBody = new ProvisionPlatformAdminAuthRequest(
                    id,
                    normalizedEmail,
                    request.getPassword());
            AuthUserResponse auth = authPlatformClient.provisionPlatformAdmin(authBody, authorizationHeader);
            return PlatformAdminResponse.from(profile, auth);
        } catch (RuntimeException ex) {
            platformAdminProfileRepository.deleteById(id);
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    public List<AuthUserResponse> listPendingEmployers(String authorizationHeader) {
        return authPlatformClient.listPendingEmployers(authorizationHeader);
    }

    public AuthUserResponse approveEmployer(UUID employerUserId, String authorizationHeader) {
        return authPlatformClient.approveEmployer(employerUserId, authorizationHeader);
    }

    public AuthUserResponse rejectEmployer(UUID employerUserId, String authorizationHeader) {
        return authPlatformClient.rejectEmployer(employerUserId, authorizationHeader);
    }
}
