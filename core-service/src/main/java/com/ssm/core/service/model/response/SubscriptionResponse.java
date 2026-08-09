package com.ssm.core.service.model.response;

import com.ssm.core.service.model.enums.SubscriptionCategory;

import java.util.UUID;

public record SubscriptionResponse(
        UUID id,
        String name,
        SubscriptionCategory category
) {
}
