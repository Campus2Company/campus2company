package com.campus2company.auth.controller;

import com.campus2company.auth.dto.request.ProvisionUniversityAdminRequest;
import com.campus2company.auth.dto.response.UserResponse;
import com.campus2company.auth.service.AuthService;
import com.campus2company.auth.service.UserAccountService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin", description = "Administrative endpoints for user account management")
public class AdminController {

    private final UserAccountService userAccountService;
    private final AuthService authService;

    @PostMapping("/university-admins")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    @Operation(summary = "Provision a university admin in auth",
            description = "Creates UNIVERSITY_ADMIN with a fixed user id (must match admin-service profile row).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Id or email already registered")
    })
    public ResponseEntity<UserResponse> provisionUniversityAdmin(
            @Valid @RequestBody ProvisionUniversityAdminRequest request) {
        UserResponse response = authService.provisionUniversityAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/employers/pending")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    @Operation(summary = "List employers awaiting approval")
    public ResponseEntity<List<UserResponse>> listPendingEmployers() {
        List<UserResponse> body = userAccountService.listPendingEmployers().stream()
                .map(UserResponse::from)
                .toList();
        return ResponseEntity.ok(body);
    }

    @PutMapping("/employers/{userId}/approve")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    @Operation(summary = "Approve an employer account",
            description = "Sets an EMPLOYER account status from PENDING_APPROVAL to ACTIVE. " +
                    "Only works for EMPLOYER role accounts that are currently pending.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employer approved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized (requires PLATFORM_ADMIN role)"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> approveEmployer(@PathVariable UUID userId) {
        UserResponse response = UserResponse.from(userAccountService.approveEmployer(userId));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/employers/{userId}/reject")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    @Operation(summary = "Reject employer registration",
            description = "Sets an EMPLOYER account from PENDING_APPROVAL to REJECTED.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employer rejected"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> rejectEmployer(@PathVariable UUID userId) {
        UserResponse response = UserResponse.from(userAccountService.rejectEmployerRegistration(userId));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{userId}/suspend")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    @Operation(summary = "Suspend a user account",
            description = "Sets account status to SUSPENDED. User will not be able to log in.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User suspended successfully"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized (requires ADMIN role)"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> suspendUser(@PathVariable UUID userId) {
        UserResponse response = UserResponse.from(userAccountService.suspendUser(userId));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{userId}/activate")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    @Operation(summary = "Activate a user account",
            description = "Sets account status to ACTIVE. User will be able to log in.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User activated successfully"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized (requires ADMIN role)"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> activateUser(@PathVariable UUID userId) {
        UserResponse response = UserResponse.from(userAccountService.activateUser(userId));
        return ResponseEntity.ok(response);
    }
}
