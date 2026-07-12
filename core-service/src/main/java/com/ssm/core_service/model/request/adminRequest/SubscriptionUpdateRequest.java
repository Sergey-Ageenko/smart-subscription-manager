package com.ssm.core_service.model.request.adminRequest;

import com.ssm.core_service.model.enums.SubscriptionCategory;
import jakarta.validation.constraints.Size;

public record SubscriptionUpdateRequest(

        @Size(max = 100, message = "Subscription name must not exceed 100 characters.")
        String name,

        SubscriptionCategory category

) {
}

