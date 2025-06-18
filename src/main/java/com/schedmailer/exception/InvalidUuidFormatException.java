package com.schedmailer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidUuidFormatException extends RuntimeException {
    public InvalidUuidFormatException(String message) {
        super(message);
    }
}
