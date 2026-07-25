package com.ssm.billing_service.model.response;

import com.ssm.billing_service.model.enums.BudgetStatus;

public record BudgetStatusResponse(
        BudgetStatus status
) {
}
