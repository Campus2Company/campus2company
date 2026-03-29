package com.campus2company.lecturer.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class SupervisionAlreadyExistsException extends ApiException {

    public SupervisionAlreadyExistsException(UUID studentId) {
        super("Student " + studentId + " is already supervised by this lecturer", HttpStatus.CONFLICT);
    }
}
