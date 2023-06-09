package com.example.oauthstarter.infrastructure.exception;

import com.example.oauthstarter.application.dto.common.ResponseDto;
import com.example.oauthstarter.domain.constant.ResponseCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ResponseDto<Object> globalAppHandler(HttpServletRequest req, AppException exp) {
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

    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Object> handleMethodArgumentNotValidException(HttpServletRequest req, BindException exp) {
        log.error("[BindException]: {} - {}", req.getRequestURI(), exp.getMessage());
        final FieldError fieldError = exp.getFieldError();
        if (Objects.nonNull(fieldError)) {
            log.error(fieldError.getDefaultMessage());
            return ResponseDto.error(ResponseCode.BAD_REQUEST, fieldError.getDefaultMessage());
        }
        return ResponseDto.error(ResponseCode.BAD_REQUEST);
    }

    @ExceptionHandler({ExpiredJwtException.class, AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDto<Object> handleExpiredJwtException(HttpServletRequest req, Exception exp) {
        if (exp instanceof ExpiredJwtException || exp.getCause() instanceof ExpiredJwtException) {
            log.error("[ExpiredJwtException]: {} - {}", req.getRequestURI(), exp.getMessage());
            return ResponseDto.error(ResponseCode.EXPIRED_TOKEN, exp.getMessage());
        }
        if (exp instanceof BadCredentialsException || exp.getCause() instanceof BadCredentialsException) {
            log.error("[BadCredentialsException]: {} - {}", req.getRequestURI(), exp.getMessage());
            return ResponseDto.error(ResponseCode.BAD_CREDENTIALS);
        }
        log.error("[AuthenticationException]: {} - {}", req.getRequestURI(), exp.getMessage());
        return ResponseDto.error(ResponseCode.UNAUTHORIZED, exp.getMessage());
    }


    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<Object> handleUncaughtException(Exception exp) {
        log.error("[InternalServerException]: {}", exp.getMessage());
        return ResponseDto.error(ResponseCode.INTERNAL_ERROR, exp.getMessage());
    }
}
