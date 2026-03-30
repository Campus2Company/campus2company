package com.campus2company.application.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public static ResourceNotFoundException application(Object id) {
        return new ResourceNotFoundException("Application not found: " + id);
    }
}
