package com.ssm.core_service.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    USER_PROFILE_NOT_FOUND_BY_ID("User's profile with id: %s was not found"),
    USER_PROFILE_IS_ALREADY_EXISTS("User's profile with id: '%s' already exists"),
    INVALID_TOKEN("Token is invalid"),
    USER_BUDGET_NOT_FOUND_BY_ID("User's budget with id: %s was not found"),
    USER_SUBSCRIPTION_NOT_FOUND_BY_ID("User's subscription with id: %s was not found"),
    INVALID_REQUEST_BODY("Invalid request body"),
    INVALID_ENUM_CONSTANTS("Invalid value '%s' for field '%s'. Allowed values: %s"),
    VALIDATION_FAILED("Validation failed"),
    SUBSCRIPTION_IS_ALREADY_EXISTS("Subscription with name: '%s' already exists");

    private final String message;

    public String getMessage(Object... args){
        return String.format(message, args);
    }
}
