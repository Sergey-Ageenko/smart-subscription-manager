package com.ssm.billing.service.feign.response;

import java.math.BigDecimal;

public record BillingBudgetSettingsResponse(
        BigDecimal monthlyLimit
) {
}
