package com.ssm.core.service.model.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    USER_PROFILE_NOT_FOUND("User's profile was not found. User id = '%s'"),
    USER_PROFILE_IS_ALREADY_EXISTS("User's profile already exists. User id = '%s'"),
    USER_BUDGET_NOT_FOUND("User's budget was not found. User id = '%s'"),
    USER_SUBSCRIPTION_NOT_FOUND("User's subscription was not found. User id = '%s', sub id = '%s'"),
    USER_SUBSCRIPTION_IS_ALREADY_EXISTS("User's subscription already exists. User id = '%s', sub id = '%s'"),
    INVALID_REQUEST_BODY("Invalid request body"),
    INVALID_ENUM_CONSTANTS("Invalid value '%s' for field '%s'. Allowed values: '%s'"),
    VALIDATION_FAILED("Validation failed"),
    SUBSCRIPTION_IS_ALREADY_EXISTS("Subscription with name: '%s' already exists"),
    SUBSCRIPTION_NOT_FOUND_BY_ID("Subscription with id: '%s' was not found"),
    USER_SUBSCRIPTION_IS_ALREADY_CANCELLED("User's subscription already cancelled. User id = '%s', sub id = '%s'"),
    USER_SUBSCRIPTION_IS_ALREADY_ACTIVATED("User's subscription already activated. User id = '%s', sub id = '%s'"),
    INVALID_GATEWAY_AUTHENTICATION("Invalid gateway authentication");
    private final String message;

    public String getMessage(Object... args){
        return String.format(message, args);
    }
}
