package com.campus2company.employer.service;

import com.campus2company.common.dto.request.CreateEmployerRequest;
import com.campus2company.employer.dto.request.CreateEmployerProfileRequest;
import com.campus2company.employer.dto.request.UpdateEmployerProfileRequest;
import com.campus2company.employer.dto.response.EmployerProfileResponse;
import com.campus2company.employer.exception.ProfileAlreadyExistsException;
import com.campus2company.employer.exception.ResourceNotFoundException;
import com.campus2company.employer.model.EmployerProfile;
import com.campus2company.employer.repository.EmployerProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployerProfileService {

    private final EmployerProfileRepository repository;

    @Transactional
    public EmployerProfileResponse createEmployerFromRegistration(CreateEmployerRequest request) {
        UUID userId = request.getAuthUserId();
        if (repository.existsByUserId(userId)) {
            throw new ProfileAlreadyExistsException();
        }

        EmployerProfile profile = EmployerProfile.builder()
                .userId(userId)
                .companyName(request.getCompanyName())
                .industry(request.getIndustry())
                .description(request.getDescription())
                .websiteUrl(request.getWebsiteUrl())
                .verified(false)
                .build();

        EmployerProfile saved = repository.save(profile);
        log.info("Created employer profile from registration for userId: {}", userId);
        return EmployerProfileResponse.from(saved);
    }

    @Transactional
    public EmployerProfileResponse createProfile(UUID userId, CreateEmployerProfileRequest request) {
        if (repository.existsByUserId(userId)) {
            throw new ProfileAlreadyExistsException();
        }

        EmployerProfile profile = EmployerProfile.builder()
                .userId(userId)
                .companyName(request.getCompanyName())
                .industry(request.getIndustry())
                .description(request.getDescription())
                .websiteUrl(request.getWebsiteUrl())
                .verified(false)
                .build();

        EmployerProfile saved = repository.save(profile);
        log.info("Created employer profile for userId: {}", userId);
        return EmployerProfileResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public EmployerProfileResponse getProfileByUserId(UUID userId) {
        EmployerProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> ResourceNotFoundException.employerProfile(userId));
        return EmployerProfileResponse.from(profile);
    }

    @Transactional(readOnly = true)
    public EmployerProfileResponse getProfileById(UUID profileId) {
        EmployerProfile profile = repository.findById(profileId)
                .orElseThrow(() -> ResourceNotFoundException.employerProfile(profileId));
        return EmployerProfileResponse.from(profile);
    }

    @Transactional(readOnly = true)
    public List<EmployerProfileResponse> getAllProfiles() {
        return repository.findAll().stream()
                .map(EmployerProfileResponse::from)
                .toList();
    }

    @Transactional
    public EmployerProfileResponse updateProfile(UUID userId, UpdateEmployerProfileRequest request) {
        EmployerProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> ResourceNotFoundException.employerProfile(userId));

        if (request.getCompanyName() != null) {
            profile.setCompanyName(request.getCompanyName());
        }
        if (request.getIndustry() != null) {
            profile.setIndustry(request.getIndustry());
        }
        if (request.getDescription() != null) {
            profile.setDescription(request.getDescription());
        }
        if (request.getWebsiteUrl() != null) {
            profile.setWebsiteUrl(request.getWebsiteUrl());
        }

        EmployerProfile saved = repository.save(profile);
        log.info("Updated employer profile for userId: {}", userId);
        return EmployerProfileResponse.from(saved);
    }

    @Transactional
    public void deleteProfile(UUID userId) {
        EmployerProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> ResourceNotFoundException.employerProfile(userId));
        repository.delete(profile);
        log.info("Deleted employer profile for userId: {}", userId);
    }
}
