package com.ssm.core_service.model.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiConstants {
    public static final String USER_ROLES = "X-User-Roles";
    public static final String USER_ID = "X-User-Id";
    public static final String USER_REGISTERED = "user-registered";
    public static final String BUDGET_UPDATED = "budget-updated";
    public static final String SUBSCRIPTION_CREATED = "subscription-created";
    public static final String SUBSCRIPTION_CANCELLED = "subscription-cancelled";
    public static final String SUBSCRIPTION_UPDATED = "subscription-updated";
}
