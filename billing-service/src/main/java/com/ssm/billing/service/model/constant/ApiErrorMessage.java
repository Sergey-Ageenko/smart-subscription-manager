package com.ssm.billing.service.model.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    INVALID_GATEWAY_AUTHENTICATION("Invalid gateway authentication");
    private final String message;

    public String getMessage(Object... args){
        return String.format(message, args);
    }
}
