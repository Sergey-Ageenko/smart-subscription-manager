package com.ssm.core_service.model.request;

import com.ssm.core_service.model.enums.BillingPeriod;
import com.ssm.core_service.model.enums.SubscriptionCategory;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record NewSubscriptionRequest(

        @NotBlank(message = "Subscription name is required.")
        @Size(max = 100, message = "Subscription name must not exceed 100 characters.")
        String name,

        @NotNull(message = "Price is required.")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
        @Digits(integer = 10, fraction = 2,
                message = "Price must contain up to 10 integer digits and 2 decimal places.")
        BigDecimal price,

        @NotNull(message = "Category is required.")
        SubscriptionCategory category,

        @NotNull(message = "Billing period is required.")
        BillingPeriod billingPeriod
) {
}
