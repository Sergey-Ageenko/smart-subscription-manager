package com.ssm.apigateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiConstants {
    public static final String PREFIX_BLACKLIST = "blacklist:";
    public static final String USER_ROLES = "X-User-Roles";
    public static final String USER_ID = "X-User-Id";
}
