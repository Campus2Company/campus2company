package com.campus2company.admin.repository;

import com.campus2company.admin.model.PlatformAdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlatformAdminProfileRepository extends JpaRepository<PlatformAdminProfile, UUID> {

    boolean existsByEmail(String email);
}
