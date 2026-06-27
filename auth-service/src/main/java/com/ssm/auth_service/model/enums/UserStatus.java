package com.ssm.auth_service.model.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserStatus {
    ACTIVE,
    BLOCKED
}
