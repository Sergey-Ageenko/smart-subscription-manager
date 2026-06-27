package com.ssm.core_service.model.response;

import java.math.BigDecimal;

public record BudgetResponse(
        BigDecimal monthlyLimit
) {
}
