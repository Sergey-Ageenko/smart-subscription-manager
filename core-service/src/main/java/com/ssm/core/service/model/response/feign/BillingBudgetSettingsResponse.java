package com.ssm.core.service.model.response.feign;

import java.math.BigDecimal;

public record BillingBudgetSettingsResponse(
        BigDecimal monthlyLimit
){
}
