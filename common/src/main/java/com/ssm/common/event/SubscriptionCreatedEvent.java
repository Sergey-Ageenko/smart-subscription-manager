package com.ssm.common.event;

import java.io.Serializable;

import java.util.UUID;

public record SubscriptionCreatedEvent (
        UUID userId,
        boolean created
        ) implements Serializable {
}