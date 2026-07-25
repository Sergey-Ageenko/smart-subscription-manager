package com.ssm.core_service.service.impl;

import com.ssm.common.exception.NotFoundException;
import com.ssm.core_service.factory.BudgetEventFactory;
import com.ssm.core_service.model.constant.ApiErrorMessage;
import com.ssm.core_service.model.entity.Budget;
import com.ssm.core_service.model.entity.OutboxEvent;
import com.ssm.core_service.model.request.user.BudgetUpdateRequest;
import com.ssm.core_service.model.response.BudgetResponse;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.repository.BudgetRepository;
import com.ssm.core_service.repository.OutboxRepository;
import com.ssm.core_service.security.UserPrincipal;
import com.ssm.core_service.service.BudgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final OutboxRepository outboxRepository;
    private final BudgetEventFactory eventFactory;


    @Override
    @Transactional(readOnly = true)
    public CoreResponse<BudgetResponse> getBudget(UUID userId) {
        Budget budget = budgetRepository.findByProfile_UserId((userId))
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_BUDGET_NOT_FOUND.getMessage(userId)
                ));
        return CoreResponse.createSuccessful(
                createResponse(budget)
        );
    }

    @Override
    @Transactional
    public CoreResponse<BudgetResponse> updateBudget(UUID userId, BudgetUpdateRequest request) {
        Budget budget = budgetRepository.findByProfile_UserId(userId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_BUDGET_NOT_FOUND.getMessage(userId)
                ));
        budget.setMonthlyLimit(request.newMonthlyLimit());
        OutboxEvent outboxEvent = eventFactory.updated(userId);
        outboxRepository.save(outboxEvent);
        log.info("Budget {} updated successfully.", budget.getId());
        return CoreResponse.createSuccessful(
                createResponse(budget)
        );
    }

    private BudgetResponse createResponse(Budget budget) {
        return new BudgetResponse(
                budget.getMonthlyLimit()
        );
    }
}
