package com.campus2company.employer.exception;

import org.springframework.http.HttpStatus;

public class ProfileAlreadyExistsException extends ApiException {

    public ProfileAlreadyExistsException() {
        super("An employer profile already exists for this account.", HttpStatus.CONFLICT);
    }
}
