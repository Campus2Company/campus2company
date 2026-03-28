package com.campus2company.notification.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public static ResourceNotFoundException notification(Object id) {
        return new ResourceNotFoundException("Notification not found: " + id);
    }
}
