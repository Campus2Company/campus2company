package com.campus2company.employer.controller;

import com.campus2company.employer.dto.request.CreateEmployerProfileRequest;
import com.campus2company.employer.dto.request.UpdateEmployerProfileRequest;
import com.campus2company.employer.dto.response.EmployerProfileResponse;
import com.campus2company.employer.security.UserPrincipal;
import com.campus2company.employer.service.EmployerProfileService;
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
@RequestMapping("/employers")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Employer Profiles", description = "Employer profile management endpoints")
public class EmployerProfileController {

    private final EmployerProfileService service;

    @PostMapping("/profile")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Create employer profile",
            description = "Create a profile for the authenticated employer account.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Profile already exists")
    })
    public ResponseEntity<EmployerProfileResponse> createProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateEmployerProfileRequest request) {
        EmployerProfileResponse response = service.createProfile(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Get own employer profile",
            description = "Returns the authenticated employer's profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<EmployerProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(service.getProfileByUserId(principal.getId()));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Update employer profile",
            description = "Update the authenticated employer's profile. Only provided fields are updated.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<EmployerProfileResponse> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateEmployerProfileRequest request) {
        return ResponseEntity.ok(service.updateProfile(principal.getId(), request));
    }

    @DeleteMapping("/profile")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Delete employer profile",
            description = "Delete the authenticated employer's profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Profile deleted"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<Void> deleteProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        service.deleteProfile(principal.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get employer profile by user ID",
            description = "Returns the employer profile for the given user ID. Accessible by any authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<EmployerProfileResponse> getByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getProfileByUserId(userId));
    }

    @GetMapping
    @Operation(summary = "List all employer profiles",
            description = "Returns all employer profiles. Accessible by any authenticated user.")
    @ApiResponse(responseCode = "200", description = "List of employer profiles")
    public ResponseEntity<List<EmployerProfileResponse>> getAllProfiles() {
        return ResponseEntity.ok(service.getAllProfiles());
    }
}
