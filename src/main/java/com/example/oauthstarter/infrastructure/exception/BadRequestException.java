package com.example.oauthstarter.infrastructure.exception;

import com.example.oauthstarter.domain.constant.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends GlobalAppException {
    public BadRequestException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }
}
