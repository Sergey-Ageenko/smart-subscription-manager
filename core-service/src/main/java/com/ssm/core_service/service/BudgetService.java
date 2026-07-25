package com.ssm.core_service.service;

import com.ssm.core_service.model.request.user.BudgetUpdateRequest;
import com.ssm.core_service.model.response.BudgetResponse;
import com.ssm.core_service.model.response.CoreResponse;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface BudgetService {
    CoreResponse<BudgetResponse> getBudget(@NotNull UUID userId);
    CoreResponse<BudgetResponse> updateBudget(@NotNull UUID userId,@NotNull BudgetUpdateRequest request);
}
