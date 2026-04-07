package com.campus2company.universityadmin.repository;

import com.campus2company.universityadmin.model.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UniversityRepository extends JpaRepository<University, UUID> {
    Optional<University> findByDomain(String domain);
    boolean existsByName(String name);
    boolean existsByDomain(String domain);
}
