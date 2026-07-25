package com.ssm.core_service.model.response.feign;

import java.math.BigDecimal;

public record BillingBudgetSettingsResponse(
        BigDecimal monthlyLimit
){
}
