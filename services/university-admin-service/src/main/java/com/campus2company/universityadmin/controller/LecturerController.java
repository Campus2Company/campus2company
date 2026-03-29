package com.campus2company.universityadmin.controller;

import com.campus2company.universityadmin.dto.request.CreateLecturerProfileRequest;
import com.campus2company.universityadmin.dto.response.LecturerProfileResponse;
import com.campus2company.universityadmin.dto.response.SupervisorAssignmentResponse;
import com.campus2company.universityadmin.security.UserPrincipal;
import com.campus2company.universityadmin.service.LecturerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/lecturers")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Lecturers", description = "Lecturer profile and assignment endpoints")
public class LecturerController {

    private final LecturerService service;

    @PostMapping("/profile")
    @PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
    @Operation(summary = "Create a lecturer profile")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lecturer profile created"),
            @ApiResponse(responseCode = "409", description = "Profile already exists")
    })
    public ResponseEntity<LecturerProfileResponse> createProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateLecturerProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createProfile(request.getUserId(), request, principal.getId()));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('LECTURER')")
    @Operation(summary = "Get own lecturer profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<LecturerProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(service.getProfileByUserId(principal.getId()));
    }

    @GetMapping("/{profileId}")
    @Operation(summary = "Get lecturer profile by profile ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<LecturerProfileResponse> getById(@PathVariable UUID profileId) {
        return ResponseEntity.ok(service.getProfileById(profileId));
    }

    @GetMapping("/assignments")
    @PreAuthorize("hasRole('LECTURER')")
    @Operation(summary = "Get all projects assigned to this lecturer")
    @ApiResponse(responseCode = "200", description = "List of assignments")
    public ResponseEntity<List<SupervisorAssignmentResponse>> getMyAssignments(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(service.getMyAssignments(principal.getId()));
    }
}
