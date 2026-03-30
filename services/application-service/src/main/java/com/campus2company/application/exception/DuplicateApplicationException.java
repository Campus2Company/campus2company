package com.campus2company.application.exception;

import org.springframework.http.HttpStatus;

public class DuplicateApplicationException extends ApiException {

    public DuplicateApplicationException() {
        super("You have already applied to this project", HttpStatus.CONFLICT);
    }
}
