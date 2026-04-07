package com.campus2company.admin.controller;

import com.campus2company.admin.dto.AuthUserResponse;
import com.campus2company.admin.dto.CreateUniversityAdminRequest;
import com.campus2company.admin.dto.UniversityAdminResponse;
import com.campus2company.admin.service.AdminOperationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PLATFORM_ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Platform admin", description = "Platform administration — university admins and employer onboarding review")
public class AdminOperationsController {

    private final AdminOperationsService adminOperationsService;

    @PostMapping("/university-admins")
    @Operation(summary = "Create a university admin",
            description = "Stores profile in admin-service and provisions UNIVERSITY_ADMIN in auth-service with the same user id.")
    public ResponseEntity<UniversityAdminResponse> createUniversityAdmin(
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody CreateUniversityAdminRequest request) {
        UniversityAdminResponse body = adminOperationsService.createUniversityAdmin(request, authorization);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/employers/pending")
    @Operation(summary = "List employer registrations awaiting approval")
    public ResponseEntity<List<AuthUserResponse>> listPendingEmployers(
            @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(adminOperationsService.listPendingEmployers(authorization));
    }

    @PutMapping("/employers/{userId}/approve")
    @Operation(summary = "Approve employer registration")
    public ResponseEntity<AuthUserResponse> approveEmployer(
            @PathVariable UUID userId,
            @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(adminOperationsService.approveEmployer(userId, authorization));
    }

    @PutMapping("/employers/{userId}/reject")
    @Operation(summary = "Reject employer registration")
    public ResponseEntity<AuthUserResponse> rejectEmployer(
            @PathVariable UUID userId,
            @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(adminOperationsService.rejectEmployer(userId, authorization));
    }
}
