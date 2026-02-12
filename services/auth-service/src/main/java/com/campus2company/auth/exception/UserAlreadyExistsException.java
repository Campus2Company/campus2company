package com.campus2company.auth.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApiException {

    public UserAlreadyExistsException(String email) {
        super("User with email '" + email + "' already exists", HttpStatus.CONFLICT);
    }
}
