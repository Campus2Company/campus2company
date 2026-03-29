package com.campus2company.universityadmin.service;

import com.campus2company.universityadmin.dto.request.CreateLecturerProfileRequest;
import com.campus2company.universityadmin.dto.response.LecturerProfileResponse;
import com.campus2company.universityadmin.dto.response.SupervisorAssignmentResponse;
import com.campus2company.universityadmin.exception.ProfileAlreadyExistsException;
import com.campus2company.universityadmin.exception.ResourceNotFoundException;
import com.campus2company.universityadmin.model.LecturerProfile;
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
public class LecturerService {

    private final LecturerProfileRepository lecturerProfileRepository;
    private final SupervisorAssignmentRepository supervisorAssignmentRepository;
    private final UniversityProfileRepository universityProfileRepository;

    @Transactional
    public LecturerProfileResponse createProfile(UUID userId, CreateLecturerProfileRequest request, UUID universityAdminUserId) {
        if (lecturerProfileRepository.existsByUserId(userId)) {
            throw new ProfileAlreadyExistsException("Lecturer profile already exists for userId: " + userId);
        }

        var universityProfile = universityProfileRepository.findByUserId(universityAdminUserId)
                .orElseThrow(() -> ResourceNotFoundException.universityProfile(universityAdminUserId));

        LecturerProfile profile = LecturerProfile.builder()
                .userId(userId)
                .name(request.getName())
                .email(request.getEmail())
                .department(request.getDepartment())
                .facultyArea(request.getFacultyArea())
                .universityProfileId(universityProfile.getId())
                .build();

        LecturerProfile saved = lecturerProfileRepository.save(profile);
        log.info("Created lecturer profile for userId: {}", userId);
        return LecturerProfileResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public LecturerProfileResponse getProfileByUserId(UUID userId) {
        return LecturerProfileResponse.from(
                lecturerProfileRepository.findByUserId(userId)
                        .orElseThrow(() -> ResourceNotFoundException.lecturerProfile(userId))
        );
    }

    @Transactional(readOnly = true)
    public LecturerProfileResponse getProfileById(UUID profileId) {
        return LecturerProfileResponse.from(
                lecturerProfileRepository.findById(profileId)
                        .orElseThrow(() -> ResourceNotFoundException.lecturerProfile(profileId))
        );
    }

    @Transactional(readOnly = true)
    public List<SupervisorAssignmentResponse> getMyAssignments(UUID userId) {
        LecturerProfile profile = lecturerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> ResourceNotFoundException.lecturerProfile(userId));

        return supervisorAssignmentRepository.findAllByLecturerProfileId(profile.getId()).stream()
                .map(SupervisorAssignmentResponse::from)
                .toList();
    }
}
