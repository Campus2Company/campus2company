package com.campus2company.universityadmin.service;

import com.campus2company.universityadmin.dto.request.CreateUniversityProfileRequest;
import com.campus2company.universityadmin.dto.request.UpdateUniversityProfileRequest;
import com.campus2company.universityadmin.dto.response.UniversityProfileResponse;
import com.campus2company.universityadmin.exception.ProfileAlreadyExistsException;
import com.campus2company.universityadmin.exception.ResourceNotFoundException;
import com.campus2company.universityadmin.model.UniversityProfile;
import com.campus2company.universityadmin.repository.UniversityProfileRepository;

import com.campus2company.universityadmin.model.University;
import com.campus2company.universityadmin.repository.UniversityRepository;
import com.campus2company.universityadmin.dto.request.CreateUniversityRequest;
import com.campus2company.universityadmin.dto.response.UniversityResponse;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityAdminService {

    private final UniversityProfileRepository universityProfileRepository;
    private final UniversityRepository universityRepository;


    @Transactional
    public UniversityProfileResponse createProfile(UUID userId, CreateUniversityProfileRequest request) {
        if (universityProfileRepository.existsByUserId(userId)) {
            throw new ProfileAlreadyExistsException("University profile already exists for userId: " + userId);
        }

        UniversityProfile profile = UniversityProfile.builder()
                .userId(userId)
                .universityId(request.getUniversityId())
                .name(request.getName())
                .domain(request.getDomain())
                .description(request.getDescription())
                .country(request.getCountry())
                .build();

        UniversityProfile saved = universityProfileRepository.save(profile);
        log.info("Created university profile for userId: {}", userId);
        return UniversityProfileResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public UniversityProfileResponse getProfileByUserId(UUID userId) {
        return UniversityProfileResponse.from(
                universityProfileRepository.findByUserId(userId)
                        .orElseThrow(() -> ResourceNotFoundException.universityProfile(userId))
        );
    }

    @Transactional
    public UniversityProfileResponse updateProfile(UUID userId, UpdateUniversityProfileRequest request) {
        UniversityProfile profile = universityProfileRepository.findByUserId(userId)
                .orElseThrow(() -> ResourceNotFoundException.universityProfile(userId));

        if (request.getName() != null) profile.setName(request.getName());
        if (request.getDescription() != null) profile.setDescription(request.getDescription());
        if (request.getCountry() != null) profile.setCountry(request.getCountry());

        UniversityProfile saved = universityProfileRepository.save(profile);
        log.info("Updated university profile for userId: {}", userId);
        return UniversityProfileResponse.from(saved);
    }


    @Transactional
    public void deleteProfile(UUID userId) {
        UniversityProfile profile = universityProfileRepository.findByUserId(userId)
                .orElseThrow(() -> ResourceNotFoundException.universityProfile(userId));
        universityProfileRepository.delete(profile);
        log.info("Deleted university profile for userId: {}", userId);
    }

    @Transactional(readOnly = true)
    public UniversityProfileResponse getProfileByDomain(String domain) {
        return UniversityProfileResponse.from(
                universityProfileRepository.findByDomain(domain)
                        .orElseThrow(() -> new ResourceNotFoundException("University profile not found for domain: " + domain))
        );
    }

    @Transactional
    public UniversityResponse createUniversity(CreateUniversityRequest request) {
        if (universityRepository.existsByName(request.getName())) {
            throw new ProfileAlreadyExistsException("University already exists with name: " + request.getName());
        }
        if (universityRepository.existsByDomain(request.getDomain())) {
            throw new ProfileAlreadyExistsException("University already exists with domain: " + request.getDomain());
        }

        University university = University.builder()
                .name(request.getName())
                .domain(request.getDomain())
                .description(request.getDescription())
                .country(request.getCountry())
                .build();

        University saved = universityRepository.save(university);
        log.info("Created university: {}", saved.getName());
        return UniversityResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<UniversityResponse> getAllUniversities() {
        return universityRepository.findAll().stream()
                .map(UniversityResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public UniversityResponse getUniversityById(UUID id) {
        return UniversityResponse.from(
                universityRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("University not found for id: " + id))
        );
    }
}