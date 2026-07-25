package com.ssm.common.event;

import java.io.Serializable;
import java.util.UUID;

public record BudgetSettingsUpdatedEvent(
        UUID userId,
        boolean updated) implements Serializable {

}
