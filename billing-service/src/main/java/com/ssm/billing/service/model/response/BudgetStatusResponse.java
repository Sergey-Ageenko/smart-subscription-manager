package com.ssm.billing.service.model.response;

import com.ssm.billing.service.model.enums.BudgetStatus;

public record BudgetStatusResponse(
        BudgetStatus status
) {
}
