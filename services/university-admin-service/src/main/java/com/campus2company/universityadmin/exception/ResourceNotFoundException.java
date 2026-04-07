package com.campus2company.universityadmin.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException universityProfile(UUID userId) {
        return new ResourceNotFoundException("University profile not found for userId: " + userId);
    }

}