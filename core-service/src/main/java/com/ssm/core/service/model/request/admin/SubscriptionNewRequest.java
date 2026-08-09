package com.ssm.core.service.model.request.admin;

import com.ssm.core.service.model.enums.SubscriptionCategory;
import jakarta.validation.constraints.*;

public record SubscriptionNewRequest(

        @NotBlank(message = "Subscription name is required.")
        @Size(max = 100, message = "Subscription name must not exceed 100 characters.")
        String name,

        @NotNull(message = "Category is required.")
        SubscriptionCategory category

) {
}
