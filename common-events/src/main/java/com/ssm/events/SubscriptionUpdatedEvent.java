package com.ssm.events;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionUpdatedEvent(
        UUID eventId,
        UUID subscriptionId,
        BigDecimal price,
        String billingPeriod) implements Serializable {
}
