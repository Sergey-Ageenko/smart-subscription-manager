package com.ssm.auth.service.advice;

import com.ssm.auth.service.model.constant.ApiErrorMessage;
import com.ssm.auth.service.utils.ApiError;
import com.ssm.common.exception.DataExistException;
import com.ssm.common.exception.NotFoundException;
import com.ssm.common.exception.UnauthorizedException;
import com.ssm.common.exception.UserBlockedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex, HttpServletRequest request) {
        log.warn("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(
                        500,
                        "INTERNAL_SERVER_ERROR",
                        ex.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now(),
                        null
                ));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(
                        401,
                        "UNAUTHORIZED",
                        ApiErrorMessage.INVALID_USER_OR_PASSWORD.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now(),
                        null
                ));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        log.warn("NotFound: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        404,
                        "NOT_FOUND",
                        ex.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now(),
                        null
                ));
    }

    @ExceptionHandler(DataExistException.class)
    public ResponseEntity<ApiError> handleDataExistException(DataExistException ex, HttpServletRequest request) {
        log.warn("Conflict: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError(
                        409,
                        "CONFLICT",
                        ex.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now(),
                        null
                ));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        log.warn("Unauthorized: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(
                        401,
                        "UNAUTHORIZED",
                        ex.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now(),
                        null
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validation failed: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ApiError apiError = new ApiError(
                400,
                "BAD_REQUEST",
                ApiErrorMessage.VALIDATION_FAILED.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now(),
                errors
        );
        return ResponseEntity.badRequest()
                .body(apiError);
    }

    @ExceptionHandler(UserBlockedException.class)
    public ResponseEntity<ApiError> handleUserBlockedException(UserBlockedException ex, HttpServletRequest request) {
        log.warn("User blocked: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiError(
                        403,
                        "USER_IS_BLOCKED",
                        ex.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now(),
                        null
                ));
    }
}