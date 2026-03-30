package com.campus2company.application.controller;

import com.campus2company.application.dto.request.CreateApplicationRequest;
import com.campus2company.application.dto.request.UpdateApplicationStatusRequest;
import com.campus2company.application.dto.response.ApplicationResponse;
import com.campus2company.application.service.ApplicationService;
import com.campus2company.common.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Applications", description = "Project application management endpoints")
public class ApplicationController {

    private final ApplicationService service;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Apply to a project",
            description = "Submit an application to a project. Only students can apply.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Application submitted"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Already applied to this project")
    })
    public ResponseEntity<ApplicationResponse> apply(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateApplicationRequest request) {
        ApplicationResponse response = service.createApplication(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/student/me")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get my applications",
            description = "Get all applications submitted by the authenticated student.")
    @ApiResponse(responseCode = "200", description = "List of applications")
    public ResponseEntity<Page<ApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return ResponseEntity.ok(service.getStudentApplications(principal.getId(), pageable));
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Get applications for a project",
            description = "Get all applications for a specific project. Only the project's employer can view.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of applications"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<Page<ApplicationResponse>> getApplicationsForProject(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return ResponseEntity.ok(service.getApplicationsForProject(principal.getId(), projectId, pageable));
    }

    @GetMapping("/employer/me")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Get all applications for my projects",
            description = "Get all applications across all projects owned by the authenticated employer.")
    @ApiResponse(responseCode = "200", description = "List of applications")
    public ResponseEntity<Page<ApplicationResponse>> getMyProjectApplications(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return ResponseEntity.ok(service.getEmployerApplications(principal.getId(), pageable));
    }

    @PutMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Accept or reject an application",
            description = "Update application status. Only the project's employer can accept/reject.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    public ResponseEntity<ApplicationResponse> updateStatus(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID applicationId,
            @Valid @RequestBody UpdateApplicationStatusRequest request) {
        return ResponseEntity.ok(service.updateApplicationStatus(principal.getId(), applicationId, request));
    }

    @PutMapping("/{applicationId}/withdraw")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Withdraw an application",
            description = "Withdraw a pending application. Only the student who applied can withdraw.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application withdrawn"),
            @ApiResponse(responseCode = "400", description = "Cannot withdraw non-pending application"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    public ResponseEntity<ApplicationResponse> withdraw(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID applicationId) {
        return ResponseEntity.ok(service.withdrawApplication(principal.getId(), applicationId));
    }

    @GetMapping("/{applicationId}")
    @Operation(summary = "Get application by ID",
            description = "Get a specific application. Accessible by any authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application details"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    public ResponseEntity<ApplicationResponse> getById(@PathVariable UUID applicationId) {
        return ResponseEntity.ok(service.getApplicationById(applicationId));
    }
}
