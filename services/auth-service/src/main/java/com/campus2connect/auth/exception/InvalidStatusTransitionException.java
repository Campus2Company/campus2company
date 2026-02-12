package com.campus2connect.auth.exception;

import com.campus2connect.auth.model.AccountStatus;
import com.campus2connect.auth.model.Role;
import org.springframework.http.HttpStatus;

public class InvalidStatusTransitionException extends ApiException {

    public InvalidStatusTransitionException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public static InvalidStatusTransitionException forApproval(Role role, AccountStatus status) {
        return new InvalidStatusTransitionException(
                "Cannot approve: user has role " + role + " and status " + status +
                        ". Only COMPANY accounts with PENDING_APPROVAL status can be approved.");
    }
}
