package com.campus2company.admin.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class LecturerNotFoundException extends ApiException {

    public LecturerNotFoundException(UUID authUserId) {
        super("No lecturer record for auth user id: " + authUserId, HttpStatus.NOT_FOUND);
    }
}
