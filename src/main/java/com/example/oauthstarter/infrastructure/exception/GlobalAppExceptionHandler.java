package com.example.oauthstarter.infrastructure.exception;

import com.example.oauthstarter.application.dto.common.ResponseDto;
import com.example.oauthstarter.domain.constant.ResponseCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalAppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GlobalAppException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ResponseDto<Object> globalAppHandler(HttpServletRequest req, GlobalAppException exp) {
        log.error("[GlobalAppException]: {} - {}", req.getRequestURI(), exp.getMessage());
        return ResponseDto.error(exp.getResponseCode(), exp.getMessage());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDto<Object> handleNotFount(HttpServletRequest req, NotFoundException exp) {
        log.error("[NotFoundException]: {} - {}", req.getRequestURI(), exp.getMessage());
        return ResponseDto.error(exp.getResponseCode(), exp.getMessage());
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Object> handleBadRequest(HttpServletRequest req, BadRequestException exp) {
        log.error("[BadRequestException]: {} - {}", req.getRequestURI(), exp.getMessage());
        return ResponseDto.error(exp.getResponseCode(), exp.getMessage());
    }

    // handle ExpiredJwtException
    @ExceptionHandler({ExpiredJwtException.class, AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDto<Object> handleExpiredJwtException(HttpServletRequest req, Exception exp) {
        if (exp instanceof ExpiredJwtException || exp.getCause() instanceof ExpiredJwtException) {
            log.error("[ExpiredJwtException]: {} - {}", req.getRequestURI(), exp.getMessage());
            return ResponseDto.error(ResponseCode.EXPIRED_TOKEN);
        }
        log.error("[AuthenticationException]: {} - {}", req.getRequestURI(), exp.getMessage());
        return ResponseDto.error(ResponseCode.UNAUTHORIZED);
    }


    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<Object> handleUncaughtException(Exception exp) {
        log.error("[InternalServerException]: {}", exp.getMessage());
        return ResponseDto.error(ResponseCode.INTERNAL_ERROR, exp.getMessage());
    }
}
