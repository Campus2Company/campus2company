package com.campus2company.employer.repository;

import com.campus2company.employer.model.EmployerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, UUID> {

    Optional<EmployerProfile> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}
