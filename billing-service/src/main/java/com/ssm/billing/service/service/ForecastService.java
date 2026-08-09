package com.ssm.billing.service.service;

import com.ssm.billing.service.model.response.BillingResponse;
import com.ssm.billing.service.model.response.BudgetStatusResponse;
import com.ssm.billing.service.model.response.ForecastResponse;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface ForecastService {

    BillingResponse<BudgetStatusResponse> getBudgetStatus(@NotNull UUID userId);

    BillingResponse<ForecastResponse> getForecast(@NotNull UUID userId);

    void calculateForecast(@NotNull UUID userId);
}
