package com.ssm.core_service.model.request;

import com.ssm.core_service.model.enums.BillingPeriod;
import com.ssm.core_service.model.enums.SubscriptionCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record SubscriptionUpdateRequest(

        UUID id,

        @Size(max = 100, message = "Subscription name must not exceed 100 characters.")
        String name,

        @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
        @Digits(integer = 10, fraction = 2,
                message = "Price must contain up to 10 integer digits and 2 decimal places.")
        BigDecimal price,

        SubscriptionCategory category,

        BillingPeriod billingPeriod
) {
}

