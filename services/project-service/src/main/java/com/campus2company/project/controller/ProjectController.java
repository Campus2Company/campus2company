package com.campus2company.project.controller;

import com.campus2company.common.enums.ProjectCategory;
import com.campus2company.common.enums.ProjectStatus;
import com.campus2company.common.enums.StudyLevel;
import com.campus2company.common.security.UserPrincipal;
import com.campus2company.project.dto.request.CreateProjectRequest;
import com.campus2company.project.dto.request.UpdateProjectRequest;
import com.campus2company.project.dto.response.ProjectResponse;
import com.campus2company.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ProjectResponse> createProject(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateProjectRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createProject(principal.getId(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ProjectResponse> updateProject(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProjectRequest request
    ) {
        return ResponseEntity.ok(service.updateProjectById(id, principal.getId(), request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ProjectResponse> archiveProject(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(service.archiveProjectById(id, principal.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.mapToResponse(service.getProjectById(id)));
    }

    // Example: /projects?status=OPEN&category=SOFTWARE_ENGINEERING&studyLevel=ANY?page=0&size=20&sort=createdAt,desc
    @GetMapping("/all")
    public ResponseEntity<Page<ProjectResponse>> getAllProjects(
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(required = false) ProjectCategory category,
            @RequestParam(required = false) StudyLevel studyLevel,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            ) {
        return ResponseEntity.ok(service.getAllProjects(
                status,
                category,
                studyLevel,
                pageable)
        );
    }
}
