package com.ssm.common.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record BudgetSettingsUpdatedEvent(
        UUID budgetId,
        BigDecimal newMonthlyLimit) implements Serializable {

}
