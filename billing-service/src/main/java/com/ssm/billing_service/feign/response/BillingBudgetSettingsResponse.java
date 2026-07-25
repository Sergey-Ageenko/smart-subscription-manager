package com.ssm.billing_service.feign.response;

import java.math.BigDecimal;

public record BillingBudgetSettingsResponse(
        BigDecimal monthlyLimit
) {
}
