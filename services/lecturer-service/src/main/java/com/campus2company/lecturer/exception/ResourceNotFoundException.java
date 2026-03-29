package com.campus2company.lecturer.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public static ResourceNotFoundException lecturerProfile(UUID userId) {
        return new ResourceNotFoundException("Lecturer profile not found for userId: " + userId);
    }

    public static ResourceNotFoundException supervision(UUID lecturerId, UUID studentId) {
        return new ResourceNotFoundException(
                "Supervision not found for lecturer " + lecturerId + " and student " + studentId);
    }
}
