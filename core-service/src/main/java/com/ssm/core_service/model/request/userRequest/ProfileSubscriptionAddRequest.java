package com.ssm.core_service.model.request.userRequest;

import com.ssm.core_service.model.enums.BillingPeriod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public record ProfileSubscriptionAddRequest(

        @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
        @Digits(integer = 10, fraction = 2,
                message = "Price must contain up to 10 integer digits and 2 decimal places.")
        BigDecimal price,

        BillingPeriod billingPeriod
){
}
