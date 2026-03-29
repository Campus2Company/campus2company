package com.campus2company.project.service;

import com.campus2company.common.enums.ProjectCategory;
import com.campus2company.common.enums.ProjectStatus;
import com.campus2company.common.enums.StudyLevel;
import com.campus2company.project.dto.request.CreateProjectRequest;
import com.campus2company.project.dto.request.UpdateProjectRequest;
import com.campus2company.project.dto.response.ProjectResponse;
import com.campus2company.project.model.Project;
import com.campus2company.project.repository.ProjectRepository;
import com.campus2company.project.repository.ProjectSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository repo;

    @Transactional
    public ProjectResponse createProject(UUID employerId, CreateProjectRequest req) {
        Project project = Project.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .employerId(employerId)
                .categories(req.getCategories())
                .applicationDeadline(req.getApplicationDeadline())
                .projectDeadline(req.getProjectDeadline())
                .status(ProjectStatus.OPEN)
                .build();

        Project saved = repo.save(project);

        return mapToResponse(saved);
    }

    @Transactional
    public ProjectResponse updateProjectById(UUID projectId, UUID employerId, UpdateProjectRequest req) {
        Project project = getProjectById(projectId);

        if (!project.getEmployerId().equals(employerId)) {
            throw new AccessDeniedException("Not owner of project");
        }
        if (project.getStatus() == ProjectStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot update archived project");
        }

        if (req.getTitle() != null) project.setTitle(req.getTitle());
        if (req.getDescription() != null) project.setDescription(req.getDescription());
        if (req.getCategories() != null) project.setCategories(req.getCategories());
        if (req.getApplicationDeadline() != null) project.setApplicationDeadline(req.getApplicationDeadline());
        if (req.getProjectDeadline() != null) project.setProjectDeadline(req.getProjectDeadline());
        if (req.getStatus() != null) project.setStatus(req.getStatus());

        Project saved = repo.save(project);
        return mapToResponse(saved);
    }

    @Transactional
    public ProjectResponse archiveProjectById(UUID projectId, UUID employerId) {
        Project project = getProjectById(projectId);
        if (!project.getEmployerId().equals(employerId)) {
            throw new AccessDeniedException("Not owner of project");
        }

        project.setStatus(ProjectStatus.ARCHIVED);
        Project saved = repo.save(project);

        return mapToResponse(saved);
    }

    public Page<ProjectResponse> getAllProjects(ProjectStatus status,
                                                ProjectCategory category,
                                                StudyLevel studyLevel,
                                                Pageable pageable) {

        Specification<Project> filters = Specification
                .where(ProjectSpecification.hasStatus(status))
                .and(ProjectSpecification.hasCategory(category))
                .and(ProjectSpecification.hasStudyLevel(studyLevel));

        return repo.findAll(filters, pageable)
                .map(this::mapToResponse);
    }

    public Project getProjectById(UUID id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Project not found: " + id));
    }

    public ProjectResponse mapToResponse(Project p) {
        return ProjectResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .description(p.getDescription())
                .employerId(p.getEmployerId())
                .categories(p.getCategories())
                .status(p.getStatus())
                .applicationDeadline(p.getApplicationDeadline())
                .projectDeadline(p.getProjectDeadline())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
