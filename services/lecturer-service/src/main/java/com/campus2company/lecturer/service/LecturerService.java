package com.campus2company.lecturer.service;

import com.campus2company.lecturer.dto.request.CreateLecturerProfileRequest;
import com.campus2company.lecturer.dto.request.UpdateLecturerProfileRequest;
import com.campus2company.lecturer.dto.response.LecturerProfileResponse;
import com.campus2company.lecturer.dto.response.SupervisionResponse;
import com.campus2company.lecturer.exception.ProfileAlreadyExistsException;
import com.campus2company.lecturer.exception.ResourceNotFoundException;
import com.campus2company.lecturer.exception.SupervisionAlreadyExistsException;
import com.campus2company.lecturer.model.LecturerProfile;
import com.campus2company.lecturer.model.Supervision;
import com.campus2company.lecturer.repository.LecturerProfileRepository;
import com.campus2company.lecturer.repository.SupervisionRepository;
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

    private final LecturerProfileRepository profileRepository;
    private final SupervisionRepository supervisionRepository;

    // Profile CRUD

    @Transactional
    public LecturerProfileResponse createProfile(UUID userId, CreateLecturerProfileRequest request) {
        if (profileRepository.existsByUserId(userId)) {
            throw new ProfileAlreadyExistsException();
        }

        LecturerProfile profile = LecturerProfile.builder()
                .userId(userId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .department(request.getDepartment())
                .faculty(request.getFaculty())
                .bio(request.getBio())
                .specializations(request.getSpecializations())
                .universityId(request.getUniversityId())
                .build();

        LecturerProfile saved = profileRepository.saveAndFlush(profile);
        log.info("Created lecturer profile for userId: {}", userId);
        return LecturerProfileResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public LecturerProfileResponse getProfileByUserId(UUID userId) {
        LecturerProfile profile = profileRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> ResourceNotFoundException.lecturerProfile(userId));
        return LecturerProfileResponse.from(profile);
    }

    @Transactional(readOnly = true)
    public List<LecturerProfileResponse> getAllProfiles(UUID universityId) {
        List<LecturerProfile> profiles = (universityId != null)
                ? profileRepository.findByUniversityIdAndIsDeletedFalse(universityId)
                : profileRepository.findByIsDeletedFalse();
        return profiles.stream()
                .map(LecturerProfileResponse::from)
                .toList();
    }

    @Transactional
    public LecturerProfileResponse updateProfile(UUID userId, UpdateLecturerProfileRequest request) {
        LecturerProfile profile = profileRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> ResourceNotFoundException.lecturerProfile(userId));

        if (request.getFirstName() != null) {
            profile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            profile.setLastName(request.getLastName());
        }
        if (request.getDepartment() != null) {
            profile.setDepartment(request.getDepartment());
        }
        if (request.getFaculty() != null) {
            profile.setFaculty(request.getFaculty());
        }
        if (request.getBio() != null) {
            profile.setBio(request.getBio());
        }
        if (request.getSpecializations() != null) {
            profile.setSpecializations(request.getSpecializations());
        }
        if (request.getSupervisorStatus() != null) {
            profile.setSupervisorStatus(request.getSupervisorStatus());
        }

        LecturerProfile saved = profileRepository.saveAndFlush(profile);
        log.info("Updated lecturer profile for userId: {}", userId);
        return LecturerProfileResponse.from(saved);
    }

    @Transactional
    public void deleteProfile(UUID userId, UUID reassignLecturerId) {
        LecturerProfile profile = profileRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> ResourceNotFoundException.lecturerProfile(userId));

        // Reassign active supervisions to another lecturer if provided
        List<Supervision> activeSupervisions = supervisionRepository.findByLecturerIdAndActiveTrue(userId);
        if (!activeSupervisions.isEmpty()) {
            if (reassignLecturerId == null) {
                throw new IllegalArgumentException(
                        "Cannot delete lecturer with " + activeSupervisions.size()
                                + " active supervisions. Provide reassignLecturerId to reassign them.");
            }

            // Verify the reassign target exists and is active
            profileRepository.findByUserIdAndIsDeletedFalse(reassignLecturerId)
                    .orElseThrow(() -> ResourceNotFoundException.lecturerProfile(reassignLecturerId));

            for (Supervision supervision : activeSupervisions) {
                supervision.setLecturerId(reassignLecturerId);
            }
            supervisionRepository.saveAll(activeSupervisions);
            log.info("Reassigned {} supervisions from {} to {}", activeSupervisions.size(), userId, reassignLecturerId);
        }

        profile.setDeleted(true);
        profileRepository.save(profile);
        log.info("Soft-deleted lecturer profile for userId: {}", userId);
    }

    // Supervision

    @Transactional
    public SupervisionResponse assignStudent(UUID lecturerId, UUID studentId) {
        // Ensure the lecturer has an active profile
        if (profileRepository.findByUserIdAndIsDeletedFalse(lecturerId).isEmpty()) {
            throw ResourceNotFoundException.lecturerProfile(lecturerId);
        }

        if (supervisionRepository.existsByLecturerIdAndStudentIdAndActiveTrue(lecturerId, studentId)) {
            throw new SupervisionAlreadyExistsException(studentId);
        }

        Supervision supervision = Supervision.builder()
                .lecturerId(lecturerId)
                .studentId(studentId)
                .active(true)
                .build();

        Supervision saved = supervisionRepository.saveAndFlush(supervision);
        log.info("Lecturer {} assigned student {}", lecturerId, studentId);
        return SupervisionResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<SupervisionResponse> getMyStudents(UUID lecturerId) {
        return supervisionRepository.findByLecturerIdAndActiveTrue(lecturerId).stream()
                .map(SupervisionResponse::from)
                .toList();
    }

    @Transactional
    public void removeStudent(UUID lecturerId, UUID studentId) {
        Supervision supervision = supervisionRepository
                .findByLecturerIdAndStudentId(lecturerId, studentId)
                .orElseThrow(() -> ResourceNotFoundException.supervision(lecturerId, studentId));

        supervision.setActive(false);
        supervisionRepository.save(supervision);
        log.info("Lecturer {} removed supervision of student {}", lecturerId, studentId);
    }
}
