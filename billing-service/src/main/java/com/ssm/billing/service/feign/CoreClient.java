package com.ssm.billing.service.feign;

import com.ssm.billing.service.feign.response.BillingBudgetSettingsResponse;
import com.ssm.billing.service.feign.response.BillingProfileSubscriptionResponse;
import com.ssm.billing.service.model.constant.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "core-service",
        url = "${services.core.url}"
)
public interface CoreClient {

    @GetMapping("/internal/budget")
    BillingBudgetSettingsResponse getBudgetSettings(@RequestHeader(ApiConstants.USER_ID) UUID userId);

    @GetMapping("/internal/subscriptions")
    List<BillingProfileSubscriptionResponse> getAllActiveSubscriptions(@RequestHeader(ApiConstants.USER_ID) UUID userId);
}