package com.example.oauthstarter.infrastructure.exception;

import com.example.oauthstarter.domain.constant.ResponseCode;
import lombok.Getter;

@Getter
public class GlobalAppException extends RuntimeException {
    private final ResponseCode responseCode;

    public GlobalAppException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public GlobalAppException(ResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }

    public GlobalAppException(String message) {
        super(message);
        this.responseCode = ResponseCode.INTERNAL_ERROR;
    }
}