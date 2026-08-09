package com.ssm.common.event;

import java.io.Serializable;
import java.util.UUID;

public record SubscriptionActivatedEvent(
        UUID userId,
        boolean activated) implements Serializable {
}
