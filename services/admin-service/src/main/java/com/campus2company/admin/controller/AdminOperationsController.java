package com.campus2company.admin.controller;

import com.campus2company.admin.dto.AuthUserResponse;
import com.campus2company.admin.dto.CreateAdminAccountRequest;
import com.campus2company.admin.dto.CreateLecturerRequest;
import com.campus2company.admin.dto.LecturerResponse;
import com.campus2company.admin.dto.ProjectLecturerAssignmentResponse;
import com.campus2company.admin.service.AdminOperationsService;
import com.campus2company.common.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PLATFORM_ADMIN','UNIVERSITY_ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin operations", description = "Provision accounts and manage project–lecturer assignments")
public class AdminOperationsController {

    private final AdminOperationsService adminOperationsService;

    @PostMapping("/admins")
    @Operation(summary = "Create another admin account",
            description = "Provisions UNIVERSITY_ADMIN or PLATFORM_ADMIN via auth-service. "
                    + "University admins cannot create platform admins (enforced in admin-service).")
    public ResponseEntity<AuthUserResponse> createAdmin(
            @AuthenticationPrincipal UserPrincipal admin,
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody CreateAdminAccountRequest request) {
        AuthUserResponse body = adminOperationsService.createAdminAccount(request, admin, authorization);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/lecturers")
    @Operation(summary = "Create a lecturer",
            description = "Provisions a LECTURER via auth-service and stores a lecturer profile in admin-service.")
    public ResponseEntity<LecturerResponse> createLecturer(
            @AuthenticationPrincipal UserPrincipal admin,
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody CreateLecturerRequest request) {
        LecturerResponse body = adminOperationsService.createLecturer(request, admin, authorization);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/projects/{projectId}/lecturers/{lecturerAuthUserId}")
    @Operation(summary = "Assign a lecturer to a project",
            description = "Links a lecturer (by auth user id) to a project id. "
                    + "Project id refers to the project domain (e.g. project-service) — stored as a UUID reference.")
    public ResponseEntity<ProjectLecturerAssignmentResponse> assignLecturerToProject(
            @PathVariable UUID projectId,
            @PathVariable UUID lecturerAuthUserId,
            @AuthenticationPrincipal UserPrincipal admin) {
        ProjectLecturerAssignmentResponse body = adminOperationsService.assignLecturerToProject(
                projectId, lecturerAuthUserId, admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/projects/{projectId}/lecturers")
    @Operation(summary = "List lecturer assignments for a project")
    public ResponseEntity<List<ProjectLecturerAssignmentResponse>> listProjectLecturers(
            @PathVariable UUID projectId) {
        return ResponseEntity.ok(adminOperationsService.listLecturersForProject(projectId));
    }
}
