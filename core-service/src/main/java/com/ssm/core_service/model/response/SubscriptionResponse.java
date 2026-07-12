package com.ssm.core_service.model.response;

import com.ssm.core_service.model.enums.SubscriptionCategory;

import java.util.UUID;

public record SubscriptionResponse(
        UUID id,
        String name,
        SubscriptionCategory category
) {
}
