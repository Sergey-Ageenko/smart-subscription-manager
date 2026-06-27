package com.ssm.auth_service.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiMessage {
    TOKEN_CREATED_OR_UPDATED("User's token has been created or updated"),
    USER_CREATED("User has been created");

    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
