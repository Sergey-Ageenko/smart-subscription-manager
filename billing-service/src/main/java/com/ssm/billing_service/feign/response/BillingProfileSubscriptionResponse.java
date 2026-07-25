package com.ssm.billing_service.feign.response;

import com.ssm.billing_service.model.enums.BillingPeriod;
import com.ssm.billing_service.model.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BillingProfileSubscriptionResponse(
        BigDecimal price,
        SubscriptionStatus status,
        BillingPeriod billingPeriod,
        LocalDate nextPaymentDate
) {
}
