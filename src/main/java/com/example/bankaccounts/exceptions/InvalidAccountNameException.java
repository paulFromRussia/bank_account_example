package com.example.bankaccounts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidAccountNameException extends RuntimeException {
    public InvalidAccountNameException(String message) {
        super(message);
    }
}
