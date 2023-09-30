package com.example.bankaccounts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidAccountIdException extends RuntimeException {
    private String accountId;

    public InvalidAccountIdException(String accountId) {
        this.accountId = accountId;
    }
}
