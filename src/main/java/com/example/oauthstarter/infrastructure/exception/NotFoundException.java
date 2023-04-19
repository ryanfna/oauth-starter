package com.example.oauthstarter.infrastructure.exception;

import com.example.oauthstarter.domain.constant.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends GlobalAppException {
    public NotFoundException() {
        super(ResponseCode.NOT_FOUND);
    }

    public NotFoundException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }
}
