package com.example.bankaccounts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NotUniqueAccountNameException extends RuntimeException {
    public NotUniqueAccountNameException(String message) {
        super(message);
    }
}
