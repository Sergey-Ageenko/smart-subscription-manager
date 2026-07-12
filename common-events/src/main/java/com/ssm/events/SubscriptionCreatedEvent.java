package com.ssm.events;

import java.io.Serializable;
import java.math.BigDecimal;

import java.util.UUID;

public record SubscriptionCreatedEvent (
        UUID subscriptionId,
        BigDecimal price,
        String billingPeriod
        ) implements Serializable {
}