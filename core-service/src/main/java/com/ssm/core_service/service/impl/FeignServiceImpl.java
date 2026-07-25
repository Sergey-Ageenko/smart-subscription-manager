package com.ssm.core_service.service.impl;

import com.ssm.common.exception.NotFoundException;
import com.ssm.core_service.model.constant.ApiErrorMessage;
import com.ssm.core_service.model.entity.Budget;
import com.ssm.core_service.model.enums.SubscriptionStatus;
import com.ssm.core_service.model.response.ProfileSubscriptionResponse;
import com.ssm.core_service.model.response.feign.BillingBudgetSettingsResponse;
import com.ssm.core_service.model.response.feign.BillingProfileSubscriptionResponse;
import com.ssm.core_service.repository.BudgetRepository;
import com.ssm.core_service.repository.ProfileSubscriptionRepository;
import com.ssm.core_service.service.FeignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeignServiceImpl implements FeignService {

    private final BudgetRepository budgetRepository;
    private final ProfileSubscriptionRepository profileSubscriptionRepository;

    @Override
    public BillingBudgetSettingsResponse getBudgetSettings(UUID userId) {
        Budget budget = budgetRepository.findByProfile_UserId(userId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_BUDGET_NOT_FOUND.getMessage(userId)
                ));
        return new BillingBudgetSettingsResponse(budget.getMonthlyLimit());
    }

    @Override
    public List<BillingProfileSubscriptionResponse> getAllActiveSubscriptions(UUID userId) {
        return profileSubscriptionRepository
                .findAllByProfile_UserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .stream()
                .map(response -> new BillingProfileSubscriptionResponse(
                        response.getPrice(),
                        response.getStatus(),
                        response.getBillingPeriod(),
                        response.getNextPaymentDate()))
                .toList();
    }
}
