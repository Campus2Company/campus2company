package com.campus2company.universityadmin.repository;

import com.campus2company.universityadmin.model.LecturerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LecturerProfileRepository extends JpaRepository<LecturerProfile, UUID> {
    Optional<LecturerProfile> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
    List<LecturerProfile> findAllByUniversityProfileId(UUID universityProfileId);
}