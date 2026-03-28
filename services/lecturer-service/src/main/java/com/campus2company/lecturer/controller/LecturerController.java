package com.campus2company.lecturer.controller;

import com.campus2company.common.security.UserPrincipal;
import com.campus2company.lecturer.dto.request.CreateLecturerProfileRequest;
import com.campus2company.lecturer.dto.request.UpdateLecturerProfileRequest;
import com.campus2company.lecturer.dto.response.LecturerProfileResponse;
import com.campus2company.lecturer.dto.response.SupervisionResponse;
import com.campus2company.lecturer.service.LecturerService;
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
public class LecturerController {

    private final LecturerService service;

    //Profile CRUD

    @PostMapping("/profile")
    @PreAuthorize("hasRole('LECTURER')")
    public ResponseEntity<LecturerProfileResponse> createProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateLecturerProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProfile(principal.getId(), request));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('LECTURER')")
    public ResponseEntity<LecturerProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(service.getProfileByUserId(principal.getId()));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('LECTURER')")
    public ResponseEntity<LecturerProfileResponse> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateLecturerProfileRequest request) {
        return ResponseEntity.ok(service.updateProfile(principal.getId(), request));
    }

    @DeleteMapping("/profile")
    @PreAuthorize("hasRole('LECTURER')")
    public ResponseEntity<Void> deleteProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        service.deleteProfile(principal.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<LecturerProfileResponse> getByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getProfileByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<LecturerProfileResponse>> getAllProfiles(
            @RequestParam(required = false) Long universityId) {
        return ResponseEntity.ok(service.getAllProfiles(universityId));
    }

    // Supervision

    @GetMapping("/students")
    @PreAuthorize("hasRole('LECTURER')")
    public ResponseEntity<List<SupervisionResponse>> getMyStudents(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(service.getMyStudents(principal.getId()));
    }

    @PostMapping("/students/{studentId}")
    @PreAuthorize("hasRole('LECTURER')")
    public ResponseEntity<SupervisionResponse> assignStudent(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID studentId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.assignStudent(principal.getId(), studentId));
    }

    @DeleteMapping("/students/{studentId}")
    @PreAuthorize("hasRole('LECTURER')")
    public ResponseEntity<Void> removeStudent(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID studentId) {
        service.removeStudent(principal.getId(), studentId);
        return ResponseEntity.noContent().build();
    }
}
