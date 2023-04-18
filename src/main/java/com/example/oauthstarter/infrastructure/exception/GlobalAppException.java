package com.example.oauthstarter.infrastructure.exception;

public class GlobalAppException extends RuntimeException {
    public GlobalAppException(String message) {
        super(message);
    }
}