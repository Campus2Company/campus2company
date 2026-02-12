package com.campus2company.auth.exception;

import com.campus2company.auth.model.AccountStatus;
import org.springframework.http.HttpStatus;

public class AccountNotActiveException extends ApiException {

    public AccountNotActiveException(AccountStatus status) {
        super("Account is " + status.name().toLowerCase().replace("_", " ") + ". Please contact support.",
                HttpStatus.FORBIDDEN);
    }
}
