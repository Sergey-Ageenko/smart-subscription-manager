package com.ssm.core_service.model.request.adminRequest;

import com.ssm.core_service.model.enums.SubscriptionCategory;
import jakarta.validation.constraints.*;

public record SubscriptionNewRequest(

        @NotBlank(message = "Subscription name is required.")
        @Size(max = 100, message = "Subscription name must not exceed 100 characters.")
        String name,

        @NotNull(message = "Category is required.")
        SubscriptionCategory category

) {
}
