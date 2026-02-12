package com.campus2connect.auth.exception;

import org.springframework.http.HttpStatus;

public class AdminRegistrationNotAllowedException extends ApiException {

    public AdminRegistrationNotAllowedException() {
        super("Admin registration is not allowed through this endpoint", HttpStatus.FORBIDDEN);
    }
}
