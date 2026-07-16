package com.ssm.apigateway.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    INVALID_TOKEN("Token is invalid"),
    TOKEN_EXPIRED("Token expired"),
    BLACKLISTED_TOKEN("Token is blacklisted");


    private final String message;

    public String getMessage(Object... args){
        return String.format(message, args);
    }
}
