package com.campus2company.admin.repository;

import com.campus2company.admin.model.ProjectLecturerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectLecturerAssignmentRepository extends JpaRepository<ProjectLecturerAssignment, UUID> {

    List<ProjectLecturerAssignment> findByProjectId(UUID projectId);

    boolean existsByProjectIdAndLecturerAuthUserId(UUID projectId, UUID lecturerAuthUserId);
}
