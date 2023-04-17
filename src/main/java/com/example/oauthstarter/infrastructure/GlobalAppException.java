package com.example.oauthstarter.infrastructure;

public class GlobalAppException extends RuntimeException {
    public GlobalAppException(String message) {
        super(message);
    }
}