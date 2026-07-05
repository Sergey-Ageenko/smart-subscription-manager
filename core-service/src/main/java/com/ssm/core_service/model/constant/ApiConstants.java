package com.ssm.core_service.model.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiConstants {
    public static final String USER_ROLES = "userRoles";
    public static final String JWT_ERROR = "jwt-error";
    public static final String USER_REGISTERED = "user-registered";
    public static final String BUDGET_UPDATED = "budget-updated";
    public static final String SUBSCRIPTION_CREATED = "subscription-created";
    public static final String SUBSCRIPTION_CANCELLED = "subscription-cancelled";
    public static final String SUBSCRIPTION_UPDATED = "subscription-updated";
}
