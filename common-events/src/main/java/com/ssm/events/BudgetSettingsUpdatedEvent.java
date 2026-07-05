package com.ssm.events;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record BudgetSettingsUpdatedEvent(
        UUID eventId,
        UUID budgetId,
        BigDecimal newMonthlyLimit) implements Serializable {

}
