package com.campus2company.lecturer.repository;

import com.campus2company.lecturer.model.LecturerProfile;
import com.campus2company.lecturer.model.SupervisorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LecturerProfileRepository extends JpaRepository<LecturerProfile, UUID> {

    Optional<LecturerProfile> findByUserIdAndIsDeletedFalse(UUID userId);

    boolean existsByUserId(UUID userId);

    List<LecturerProfile> findByIsDeletedFalse();

    List<LecturerProfile> findByUniversityIdAndIsDeletedFalse(Long universityId);

    List<LecturerProfile> findByUniversityIdAndSupervisorStatusAndIsDeletedFalse(Long universityId, SupervisorStatus supervisorStatus);
}
