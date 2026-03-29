package com.campus2company.universityadmin.service;

import com.campus2company.universityadmin.dto.request.AssignLecturerRequest;
import com.campus2company.universityadmin.dto.request.CreateUniversityProfileRequest;
import com.campus2company.universityadmin.dto.request.UpdateUniversityProfileRequest;
import com.campus2company.universityadmin.dto.response.LecturerProfileResponse;
import com.campus2company.universityadmin.dto.response.SupervisorAssignmentResponse;
import com.campus2company.universityadmin.dto.response.UniversityProfileResponse;
import com.campus2company.universityadmin.exception.AssignmentAlreadyExistsException;
import com.campus2company.universityadmin.exception.ProfileAlreadyExistsException;
import com.campus2company.universityadmin.exception.ResourceNotFoundException;
import com.campus2company.universityadmin.model.AssignmentStatus;
import com.campus2company.universityadmin.model.SupervisorAssignment;
import com.campus2company.universityadmin.model.UniversityProfile;
import com.campus2company.universityadmin.repository.LecturerProfileRepository;
import com.campus2company.universityadmin.repository.SupervisorAssignmentRepository;
import com.campus2company.universityadmin.repository.UniversityProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityAdminService {

    private final UniversityProfileRepository universityProfileRepository;
    private final LecturerProfileRepository lecturerProfileRepository;
    private final SupervisorAssignmentRepository supervisorAssignmentRepository;

    @Transactional
    public UniversityProfileResponse createProfile(UUID userId, CreateUniversityProfileRequest request) {
        if (universityProfileRepository.existsByUserId(userId)) {
            throw new ProfileAlreadyExistsException("University profile already exists for userId: " + userId);
        }

        UniversityProfile profile = UniversityProfile.builder()
                .userId(userId)
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
    public SupervisorAssignmentResponse assignLecturer(AssignLecturerRequest request) {
        if (supervisorAssignmentRepository.existsByProjectId(request.getProjectId())) {
            throw new AssignmentAlreadyExistsException("A supervisor is already assigned to projectId: " + request.getProjectId());
        }

        SupervisorAssignment assignment = SupervisorAssignment.builder()
                .lecturerProfileId(request.getLecturerProfileId())
                .projectId(request.getProjectId())
                .studentUserId(request.getStudentUserId())
                .status(AssignmentStatus.ACTIVE)
                .build();

        SupervisorAssignment saved = supervisorAssignmentRepository.save(assignment);
        log.info("Assigned lecturer {} to project {}", request.getLecturerProfileId(), request.getProjectId());
        return SupervisorAssignmentResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<SupervisorAssignmentResponse> getAllAssignments() {
        return supervisorAssignmentRepository.findAll().stream()
                .map(SupervisorAssignmentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LecturerProfileResponse> getAllLecturers(UUID userId) {
        UniversityProfile profile = universityProfileRepository.findByUserId(userId)
                .orElseThrow(() -> ResourceNotFoundException.universityProfile(userId));

        return lecturerProfileRepository.findAllByUniversityProfileId(profile.getId()).stream()
                .map(LecturerProfileResponse::from)
                .toList();
    }
}
