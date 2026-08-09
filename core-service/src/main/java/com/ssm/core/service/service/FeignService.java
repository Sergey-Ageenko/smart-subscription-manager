package com.ssm.core.service.service;

import com.ssm.core.service.model.response.feign.BillingBudgetSettingsResponse;
import com.ssm.core.service.model.response.feign.BillingProfileSubscriptionResponse;

import java.util.List;
import java.util.UUID;

public interface FeignService {
    BillingBudgetSettingsResponse getBudgetSettings(UUID userId);
    List<BillingProfileSubscriptionResponse> getAllActiveSubscriptions(UUID userId);
}
