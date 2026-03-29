package com.campus2company.universityadmin.repository;

import com.campus2company.universityadmin.model.SupervisorAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SupervisorAssignmentRepository extends JpaRepository<SupervisorAssignment, UUID> {
    List<SupervisorAssignment> findAllByLecturerProfileId(UUID lecturerProfileId);
    List<SupervisorAssignment> findAllByStudentUserId(UUID studentUserId);
    boolean existsByProjectId(UUID projectId);
}