package com.campus2company.auth.controller;

import com.campus2company.auth.dto.response.UserResponse;
import com.campus2company.auth.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PLATFORM_ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin", description = "Administrative endpoints for user account management")
public class AdminController {

    private final UserAccountService userAccountService;

    @PutMapping("/employers/{userId}/approve")
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

    @PutMapping("/users/{userId}/suspend")
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
