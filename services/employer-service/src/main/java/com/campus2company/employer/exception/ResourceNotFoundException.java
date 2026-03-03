package com.campus2company.employer.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public static ResourceNotFoundException employerProfile(Object id) {
        return new ResourceNotFoundException("Employer profile not found: " + id);
    }
}
