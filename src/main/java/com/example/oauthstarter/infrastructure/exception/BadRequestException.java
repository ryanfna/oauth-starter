package com.example.oauthstarter.infrastructure.exception;

import com.example.oauthstarter.domain.constant.ResponseCode;

public class BadRequestException extends AppException {
    public BadRequestException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }
}
