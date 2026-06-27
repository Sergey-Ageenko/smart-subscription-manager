package com.ssm.core_service.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BudgetUpdateRequest(
        @NotNull(message = "Monthly limit is required.")
        @DecimalMin(value = "0.00", inclusive = true,
                message = "Monthly limit cannot be negative.")
        @Digits(integer = 10, fraction = 2,
                message = "Monthly limit must contain up to 10 integer digits and 2 decimal places.")
        BigDecimal newMonthlyLimit
) {
}
