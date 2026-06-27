package com.ssm.core_service.service;

import com.ssm.core_service.model.request.BudgetUpdateRequest;
import com.ssm.core_service.model.response.BudgetResponse;
import com.ssm.core_service.model.response.CoreResponse;

import java.util.UUID;

public interface BudgetService {
    CoreResponse<BudgetResponse> getBudget(UUID profileId);
    CoreResponse<BudgetResponse> updateBudget(UUID profileId, BudgetUpdateRequest request);

}
