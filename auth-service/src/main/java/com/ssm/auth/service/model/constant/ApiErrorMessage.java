package com.ssm.auth.service.model.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    USER_NOT_FOUND_BY_USERNAME("User with username: %s was not found"),
    USER_NOT_FOUND_BY_ID("User with id: %s was not found"),
    USER_WITH_USERNAME_ALREADY_EXISTS("User with username: '%s' already exists"),
    ROLE_NOT_FOUND("Role with name: '%s' not found"),
    REFRESH_TOKEN_INVALID_OR_EXPIRED("Refresh token is invalid or expired"),
    INVALID_USER_OR_PASSWORD("Invalid username or password. Try again"),
    USER_IS_BLOCKED("User with username: %s is blocked"),
    VALIDATION_FAILED("Validation failed"),
    INVALID_AUTHORIZATION_HEADER("Invalid Authorization header");

    private final String message;

    public String getMessage(Object... args){
        return String.format(message, args);
    }
}
