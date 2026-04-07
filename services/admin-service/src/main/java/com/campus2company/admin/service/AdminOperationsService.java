package com.campus2company.admin.service;

import com.campus2company.admin.client.AuthPlatformClient;
import com.campus2company.admin.dto.AuthUserResponse;
import com.campus2company.admin.dto.CreateUniversityAdminRequest;
import com.campus2company.admin.dto.ProvisionUniversityAdminAuthRequest;
import com.campus2company.admin.dto.UniversityAdminResponse;
import com.campus2company.admin.exception.ApiException;
import com.campus2company.admin.model.UniversityAdminProfile;
import com.campus2company.admin.repository.UniversityAdminProfileRepository;
import lombok.RequiredArgsConstructor;
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
    private final UniversityAdminProfileRepository universityAdminProfileRepository;

    @Transactional
    public UniversityAdminResponse createUniversityAdmin(
            CreateUniversityAdminRequest request,
            String authorizationHeader) {

        String normalizedEmail = request.getEmail().toLowerCase().trim();
        if (universityAdminProfileRepository.existsByEmail(normalizedEmail)) {
            throw new ApiException("A university admin with this email already exists", HttpStatus.CONFLICT);
        }

        UUID id = UUID.randomUUID();
        UniversityAdminProfile profile = UniversityAdminProfile.builder()
                .id(id)
                .email(normalizedEmail)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .universityId(request.getUniversityId())
                .createdAt(Instant.now())
                .build();

        universityAdminProfileRepository.save(profile);

        try {
            ProvisionUniversityAdminAuthRequest authBody = new ProvisionUniversityAdminAuthRequest(
                    id,
                    normalizedEmail,
                    request.getPassword());
            AuthUserResponse auth = authPlatformClient.provisionUniversityAdmin(authBody, authorizationHeader);
            return UniversityAdminResponse.from(profile, auth);
        } catch (RuntimeException ex) {
            universityAdminProfileRepository.deleteById(id);
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
