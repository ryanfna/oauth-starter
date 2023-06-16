package com.example.oauthstarter.infrastructure.exception;

import com.example.oauthstarter.domain.constant.ResponseCode;

public class NotFoundException extends AppException {
    public NotFoundException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }
}
