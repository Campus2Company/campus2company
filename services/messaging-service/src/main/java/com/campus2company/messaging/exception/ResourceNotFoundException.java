package com.campus2company.messaging.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public static ResourceNotFoundException conversation(Object id) {
        return new ResourceNotFoundException("Conversation not found: " + id);
    }

    public static ResourceNotFoundException message(Object id) {
        return new ResourceNotFoundException("Message not found: " + id);
    }
}
