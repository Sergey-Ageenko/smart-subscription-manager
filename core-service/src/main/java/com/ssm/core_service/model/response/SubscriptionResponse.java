package com.ssm.core_service.model.response;

import com.ssm.core_service.model.enums.BillingPeriod;
import com.ssm.core_service.model.enums.SubscriptionCategory;
import com.ssm.core_service.model.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionResponse(
        UUID id,
        String name,
        BigDecimal price,
        SubscriptionCategory category,
        BillingPeriod billingPeriod,
        SubscriptionStatus status,
        LocalDate nextPaymentDate
) {
}
