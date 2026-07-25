package com.ssm.common.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record BudgetSettingsUpdatedEvent(
        UUID userId,
        boolean updated) implements Serializable {

}
