package com.campus2company.lecturer.repository;

import com.campus2company.lecturer.model.LecturerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LecturerProfileRepository extends JpaRepository<LecturerProfile, UUID> {

    Optional<LecturerProfile> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}
