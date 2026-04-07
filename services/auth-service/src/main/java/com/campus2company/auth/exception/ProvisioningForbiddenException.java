package com.campus2company.auth.exception;

import org.springframework.http.HttpStatus;

public class ProvisioningForbiddenException extends ApiException {

    public ProvisioningForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
