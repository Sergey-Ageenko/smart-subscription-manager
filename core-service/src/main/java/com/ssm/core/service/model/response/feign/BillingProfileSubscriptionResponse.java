package com.ssm.core.service.model.response.feign;

import com.ssm.core.service.model.enums.BillingPeriod;
import com.ssm.core.service.model.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BillingProfileSubscriptionResponse(
        BigDecimal price,
        SubscriptionStatus status,
        BillingPeriod billingPeriod,
        LocalDate nextPaymentDate
) {
}
