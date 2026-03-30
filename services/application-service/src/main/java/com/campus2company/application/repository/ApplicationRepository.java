package com.campus2company.application.repository;

import com.campus2company.application.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    Page<Application> findByStudentId(UUID studentId, Pageable pageable);

    Page<Application> findByProjectId(UUID projectId, Pageable pageable);

    Page<Application> findByEmployerId(UUID employerId, Pageable pageable);

    boolean existsByStudentIdAndProjectId(UUID studentId, UUID projectId);

    Optional<Application> findByIdAndStudentId(UUID id, UUID studentId);

    Optional<Application> findByIdAndEmployerId(UUID id, UUID employerId);
}
