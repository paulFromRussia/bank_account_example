package com.example.bankaccounts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {
    private long accountId;
    public AccountNotFoundException(long accountId) {
        this.accountId = accountId;
    }
}
