package com.example.oauthstarter.infrastructure.exception;

import com.example.oauthstarter.domain.constant.ResponseCode;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final ResponseCode responseCode;

    public AppException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public AppException(ResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }

    public AppException(String message) {
        super(message);
        this.responseCode = ResponseCode.INTERNAL_ERROR;
    }
}