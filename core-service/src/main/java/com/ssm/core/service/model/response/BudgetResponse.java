package com.ssm.core.service.model.response;

import java.math.BigDecimal;

public record BudgetResponse(
        BigDecimal monthlyLimit
) {
}
