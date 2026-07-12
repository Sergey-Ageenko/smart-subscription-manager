package com.ssm.events;

import java.io.Serializable;
import java.util.UUID;

public record SubscriptionCancelledEvent(
        UUID subscriptionId,
        String status) implements Serializable {
}
