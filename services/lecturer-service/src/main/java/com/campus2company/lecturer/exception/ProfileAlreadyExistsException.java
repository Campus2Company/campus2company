package com.campus2company.lecturer.exception;

import org.springframework.http.HttpStatus;

public class ProfileAlreadyExistsException extends ApiException {

    public ProfileAlreadyExistsException() {
        super("A lecturer profile already exists for this account", HttpStatus.CONFLICT);
    }
}
