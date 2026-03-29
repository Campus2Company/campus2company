package com.campus2company.universityadmin.controller;

import com.campus2company.universityadmin.dto.request.AssignLecturerRequest;
import com.campus2company.universityadmin.dto.request.CreateUniversityProfileRequest;
import com.campus2company.universityadmin.dto.request.UpdateUniversityProfileRequest;
import com.campus2company.universityadmin.dto.response.LecturerProfileResponse;
import com.campus2company.universityadmin.dto.response.SupervisorAssignmentResponse;
import com.campus2company.universityadmin.dto.response.UniversityProfileResponse;
import com.campus2company.universityadmin.security.UserPrincipal;
import com.campus2company.universityadmin.service.UniversityAdminService;
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

@RestController
@RequestMapping("/university")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "University Admin", description = "University admin profile and supervisor assignment endpoints")
public class UniversityAdminController {

    private final UniversityAdminService service;

    @PostMapping("/profile")
    @PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
    @Operation(summary = "Create university profile")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profile created successfully"),
            @ApiResponse(responseCode = "409", description = "Profile already exists")
    })
    public ResponseEntity<UniversityProfileResponse> createProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateUniversityProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProfile(principal.getId(), request));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
    @Operation(summary = "Get own university profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<UniversityProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(service.getProfileByUserId(principal.getId()));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
    @Operation(summary = "Update university profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<UniversityProfileResponse> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateUniversityProfileRequest request) {
        return ResponseEntity.ok(service.updateProfile(principal.getId(), request));
    }

    @PostMapping("/assignments")
    @PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
    @Operation(summary = "Assign a lecturer to a project")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lecturer assigned successfully"),
            @ApiResponse(responseCode = "409", description = "Assignment already exists")
    })
    public ResponseEntity<SupervisorAssignmentResponse> assignLecturer(
            @Valid @RequestBody AssignLecturerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.assignLecturer(request));
    }

    @GetMapping("/assignments")
    @PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
    @Operation(summary = "Get all supervisor assignments")
    @ApiResponse(responseCode = "200", description = "List of assignments")
    public ResponseEntity<List<SupervisorAssignmentResponse>> getAllAssignments() {
        return ResponseEntity.ok(service.getAllAssignments());
    }

    @GetMapping("/lecturers")
    @PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
    @Operation(summary = "Get all lecturers at this university")
    @ApiResponse(responseCode = "200", description = "List of lecturers")
    public ResponseEntity<List<LecturerProfileResponse>> getAllLecturers(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(service.getAllLecturers(principal.getId()));
    }
}
