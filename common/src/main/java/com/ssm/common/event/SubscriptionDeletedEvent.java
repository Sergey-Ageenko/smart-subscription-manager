package com.ssm.common.event;

import java.io.Serializable;
import java.util.UUID;

public record SubscriptionDeletedEvent(
        UUID userId,
        boolean deleted) implements Serializable {
}
