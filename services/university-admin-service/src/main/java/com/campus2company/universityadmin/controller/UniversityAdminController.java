package com.campus2company.universityadmin.controller;

import com.campus2company.universityadmin.dto.request.CreateUniversityProfileRequest;
import com.campus2company.universityadmin.dto.request.UpdateUniversityProfileRequest;
import com.campus2company.universityadmin.dto.response.UniversityProfileResponse;
import com.campus2company.universityadmin.service.UniversityAdminService;
import com.campus2company.common.security.UserPrincipal;
import com.campus2company.universityadmin.dto.request.CreateUniversityRequest;
import com.campus2company.universityadmin.dto.response.UniversityResponse;

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
@RequestMapping("/university")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "University Admin", description = "University admin profile management endpoints")
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

    @DeleteMapping("/profile")
    @PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
    @Operation(summary = "Delete university profile")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Profile deleted"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<Void> deleteProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        service.deleteProfile(principal.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/domain/{domain}")
    @Operation(summary = "Get university profile by domain")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<UniversityProfileResponse> getByDomain(@PathVariable String domain) {
        return ResponseEntity.ok(service.getProfileByDomain(domain));
    }

    @PostMapping
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    @Operation(summary = "Create a university")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "University created successfully"),
            @ApiResponse(responseCode = "409", description = "University already exists")
    })
    public ResponseEntity<UniversityResponse> createUniversity(
            @Valid @RequestBody CreateUniversityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createUniversity(request));
    }

    @GetMapping
    @Operation(summary = "Get all universities")
    @ApiResponse(responseCode = "200", description = "List of universities")
    public ResponseEntity<List<UniversityResponse>> getAllUniversities() {
        return ResponseEntity.ok(service.getAllUniversities());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get university by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "University retrieved"),
            @ApiResponse(responseCode = "404", description = "University not found")
    })
    public ResponseEntity<UniversityResponse> getUniversityById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getUniversityById(id));
    }
}