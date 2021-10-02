package com.example.amit.springbootjwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException(String msg) {
        super(msg);
    }
}
