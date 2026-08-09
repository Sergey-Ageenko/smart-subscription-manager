package com.ssm.billing.service.model.response;

import com.ssm.billing.service.model.enums.BudgetStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ForecastResponse (
        BigDecimal monthlyExpenses,
        BigDecimal remainingBudget,
        BudgetStatus status,
        LocalDateTime calculatedAt
){
}
