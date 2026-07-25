package com.ssm.common.event;

import java.io.Serializable;
import java.util.UUID;

public record SubscriptionCancelledEvent(
        UUID userId,
        boolean cancelled) implements Serializable {
}
