package com.campus2company.admin.repository;

import com.campus2company.admin.model.UniversityAdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UniversityAdminProfileRepository extends JpaRepository<UniversityAdminProfile, UUID> {

    boolean existsByEmail(String email);
}
