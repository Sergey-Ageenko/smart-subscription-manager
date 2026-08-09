package com.ssm.core.service.model.response;

import com.ssm.core.service.model.enums.BillingPeriod;
import com.ssm.core.service.model.enums.SubscriptionCategory;
import com.ssm.core.service.model.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ProfileSubscriptionResponse(
        UUID id,
        String name,
        SubscriptionCategory category,
        BigDecimal price,
        SubscriptionStatus status,
        BillingPeriod billingPeriod,
        LocalDate nextPaymentDate) {
}
