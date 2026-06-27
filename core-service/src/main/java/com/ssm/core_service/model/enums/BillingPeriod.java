package com.ssm.core_service.model.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum BillingPeriod {
    WEEKLY,
    MONTHLY,
    YEARLY
}