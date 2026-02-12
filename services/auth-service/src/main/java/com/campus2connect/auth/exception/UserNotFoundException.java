package com.campus2connect.auth.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotFoundException extends ApiException {

    public UserNotFoundException(UUID userId) {
        super("User with id '" + userId + "' not found", HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String email) {
        super("User with email '" + email + "' not found", HttpStatus.NOT_FOUND);
    }
}
