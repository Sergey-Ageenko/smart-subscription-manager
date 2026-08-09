package com.ssm.billing.service.service.impl;

import com.ssm.billing.service.feign.CoreClient;
import com.ssm.billing.service.feign.response.BillingBudgetSettingsResponse;
import com.ssm.billing.service.feign.response.BillingProfileSubscriptionResponse;
import com.ssm.billing.service.model.entity.ExpenseForecast;
import com.ssm.billing.service.model.enums.BudgetStatus;
import com.ssm.billing.service.model.response.BillingResponse;
import com.ssm.billing.service.model.response.BudgetStatusResponse;
import com.ssm.billing.service.model.response.ForecastResponse;
import com.ssm.billing.service.repository.ExpenseForecastRepository;
import com.ssm.billing.service.service.ForecastService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ForecastServiceImpl implements ForecastService {

    private static final BigDecimal LOW_BALANCE_PERCENT = new BigDecimal("0.2");

    private final CoreClient coreClient;
    private final ExpenseForecastRepository expenseForecastRepository;

    @Override
    @Transactional(readOnly = true)
    public BillingResponse<BudgetStatusResponse> getBudgetStatus(UUID userId) {
        ExpenseForecast forecast = expenseForecastRepository.findByUserId(userId)
                .orElseGet(this::createEmptyForecast);
        return BillingResponse.createSuccessful(new BudgetStatusResponse(forecast.getStatus()));
    }

    @Override
    @Transactional(readOnly = true)
    public BillingResponse<ForecastResponse> getForecast(UUID userId) {
        ExpenseForecast forecast = expenseForecastRepository.findByUserId(userId)
                .orElseGet(this::createEmptyForecast);
        return BillingResponse.createSuccessful(new ForecastResponse(
                forecast.getMonthlyExpenses(),
                forecast.getRemainingBudget(),
                forecast.getStatus(),
                forecast.getCalculatedAt()));
    }

    @Override
    @Transactional
    public void calculateForecast(UUID userId) {
        ExpenseForecast forecast = expenseForecastRepository.findByUserId(userId)
                .orElse(ExpenseForecast.builder()
                        .userId(userId)
                        .build());
        List<BillingProfileSubscriptionResponse> subscriptionResponses = coreClient.getAllActiveSubscriptions(userId);
        BillingBudgetSettingsResponse settings = coreClient.getBudgetSettings(userId);
        BigDecimal monthly = BigDecimal.ZERO;
        for (BillingProfileSubscriptionResponse sub : subscriptionResponses) {
            monthly = monthly.add(calculateMonthlyExpenses(sub));
        }
        BigDecimal limit = settings.monthlyLimit();
        BigDecimal remainingBudget = limit.subtract(monthly);
        BudgetStatus status;
        if (remainingBudget.compareTo(BigDecimal.ZERO) < 0) {
            status = BudgetStatus.EXCEEDED;
        } else if (remainingBudget.compareTo(limit.multiply(LOW_BALANCE_PERCENT)) <= 0) {
            status = BudgetStatus.WARNING;
        } else {
            status = BudgetStatus.OK;
        }
        forecast.setMonthlyExpenses(monthly);
        forecast.setRemainingBudget(remainingBudget);
        forecast.setStatus(status);
        forecast.setCalculatedAt(LocalDateTime.now());
        expenseForecastRepository.save(forecast);
        log.info("Forecast calculated for user with id = {}", userId);
    }

    private ExpenseForecast createEmptyForecast(){
        return ExpenseForecast.builder()
                .monthlyExpenses(BigDecimal.ZERO)
                .status(BudgetStatus.NO_STATUS)
                .build();
    }


    private BigDecimal calculateMonthlyExpenses(BillingProfileSubscriptionResponse subscription) {
        return switch (subscription.billingPeriod()) {
            case WEEKLY -> subscription.price()
                    .multiply(BigDecimal.valueOf(52))
                    .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            case MONTHLY -> subscription.price();
            case YEARLY -> subscription.price().divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        };
    }
}
