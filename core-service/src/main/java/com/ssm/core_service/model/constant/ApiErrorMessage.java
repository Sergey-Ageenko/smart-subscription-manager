package com.ssm.core_service.model.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    USER_PROFILE_NOT_FOUND_BY_ID("User's profile with id: '%s' was not found"),
    USER_PROFILE_IS_ALREADY_EXISTS("User's profile with id: '%s' already exists"),
    USER_BUDGET_NOT_FOUND_BY_ID("User's budget with id: '%s' was not found"),
    USER_SUBSCRIPTION_NOT_FOUND_BY_ID("User's subscription with id: '%s' was not found"),
    USER_SUBSCRIPTION_IS_ALREADY_EXISTS("User's subscription with id: '%s' already exists"),
    INVALID_REQUEST_BODY("Invalid request body"),
    INVALID_ENUM_CONSTANTS("Invalid value '%s' for field '%s'. Allowed values: '%s'"),
    VALIDATION_FAILED("Validation failed"),
    SUBSCRIPTION_IS_ALREADY_EXISTS("Subscription with name: '%s' already exists"),
    EVENT_IS_ALREADY_EXISTS ("Event with id: '%s' already exists"),
    SUBSCRIPTION_NOT_FOUND_BY_ID("Subscription with id: '%s' was not found"),
    USER_SUBSCRIPTION_IS_ALREADY_CANCELLED("User's subscription with id: '%s' already cancelled"),
    INVALID_GATEWAY_AUTHENTICATION("Invalid gateway authentication");
    private final String message;

    public String getMessage(Object... args){
        return String.format(message, args);
    }
}
