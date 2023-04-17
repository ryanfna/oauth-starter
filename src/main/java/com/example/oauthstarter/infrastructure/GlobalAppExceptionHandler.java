package com.example.oauthstarter.infrastructure;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalAppExceptionHandler {

    @ExceptionHandler(GlobalAppException.class)
    public ResponseEntity<String> handleGlobalAppException(GlobalAppException e) {
        return ResponseEntity.internalServerError()
                .body(e.getMessage());
    }
}
