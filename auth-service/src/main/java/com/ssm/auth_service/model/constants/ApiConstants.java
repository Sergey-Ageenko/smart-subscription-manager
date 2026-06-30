package com.ssm.auth_service.model.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiConstants {
    public static final String USER_ID = "userId";
    public static final String USER_ROLES = "userRoles";
    public static final String PREFIX_REFRESH = "refresh:";
    public static final String PREFIX_USER_REFRESH = "user-refresh:";
    public static final String ISSUER = "auth-service";
    public static final String AUDIENCE = "core-service";
    public static final String REFRESH_TOKEN = "refresh-token";
    public static final String JWT_ERROR = "jwt-error";
    public static final String USER_REGISTERED = "user-registered";
}
