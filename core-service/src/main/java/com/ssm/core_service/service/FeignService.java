package com.ssm.core_service.service;

import com.ssm.core_service.model.response.feign.BillingBudgetSettingsResponse;
import com.ssm.core_service.model.response.feign.BillingProfileSubscriptionResponse;

import java.util.List;
import java.util.UUID;

public interface FeignService {
    BillingBudgetSettingsResponse getBudgetSettings(UUID userId);
    List<BillingProfileSubscriptionResponse> getAllActiveSubscriptions(UUID userId);
}
