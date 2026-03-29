package com.campus2company.universityadmin.repository;

import com.campus2company.universityadmin.model.UniversityProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UniversityProfileRepository extends JpaRepository<UniversityProfile, UUID> {
    Optional<UniversityProfile> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
}