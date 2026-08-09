package com.ssm.billing.service.feign.response;

import com.ssm.billing.service.model.enums.BillingPeriod;
import com.ssm.billing.service.model.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BillingProfileSubscriptionResponse(
        BigDecimal price,
        SubscriptionStatus status,
        BillingPeriod billingPeriod,
        LocalDate nextPaymentDate
) {
}
