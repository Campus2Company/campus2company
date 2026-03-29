package com.campus2company.auth.controller;

import com.campus2company.auth.dto.request.EmployerRegisterRequest;
import com.campus2company.auth.dto.request.LoginRequest;
import com.campus2company.auth.dto.request.RegisterRequest;
import com.campus2company.auth.dto.request.StudentRegisterRequest;
import com.campus2company.auth.dto.response.LoginResponse;
import com.campus2company.auth.dto.response.UserResponse;
import com.campus2company.auth.security.UserPrincipal;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration, login, and authentication endpoints")
public class AuthController {

    private final AuthService authService;
    private final UserAccountService userAccountService;

    @PostMapping("/register/student")
    @Operation(summary = "Register a new Student")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Admin registration not allowed"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<UserResponse> register(@Valid @RequestBody StudentRegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register/employer")
    @Operation(summary = "Register a new Employer",
            description = "Register a new EMPLOYER account with company details. Account will be PENDING_APPROVAL until an admin approves it.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employer registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<UserResponse> registerEmployer(@Valid @RequestBody EmployerRegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login and obtain JWT token",
            description = "Authenticate with email and password. Returns JWT access token on success.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "403", description = "Account not active (pending approval or suspended)")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user details",
            description = "Returns the authenticated user's profile based on JWT token claims and database record.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User details retrieved"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        // Get fresh data from DB to ensure accuracy
        UserResponse response = UserResponse.from(userAccountService.findById(principal.getId()));
        return ResponseEntity.ok(response);
    }
}
