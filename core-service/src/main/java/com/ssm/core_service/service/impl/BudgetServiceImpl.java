package com.ssm.core_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssm.core_service.exception.NotFoundException;
import com.ssm.core_service.factory.BudgetEventFactory;
import com.ssm.core_service.model.constant.ApiErrorMessage;
import com.ssm.core_service.model.entity.Budget;
import com.ssm.core_service.model.entity.OutboxEvent;
import com.ssm.core_service.model.request.userRequest.BudgetUpdateRequest;
import com.ssm.core_service.model.response.BudgetResponse;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.repository.BudgetRepository;
import com.ssm.core_service.repository.OutboxRepository;
import com.ssm.core_service.service.BudgetService;
import com.ssm.events.BudgetSettingsUpdatedEvent;
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
    public CoreResponse<BudgetResponse> getBudget(UUID profileId) {
        Budget budget = budgetRepository.findByProfile_Id(profileId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_BUDGET_NOT_FOUND_BY_ID.getMessage(profileId)
                ));
        return CoreResponse.createSuccessful(
                createResponse(budget)
        );
    }

    @Override
    @Transactional
    public CoreResponse<BudgetResponse> updateBudget(UUID profileId, BudgetUpdateRequest request) throws JsonProcessingException {
        Budget budget = budgetRepository.findByProfile_Id(profileId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_BUDGET_NOT_FOUND_BY_ID.getMessage(profileId)
                ));
        budget.setMonthlyLimit(request.newMonthlyLimit());
        OutboxEvent outboxEvent = eventFactory.updated(budget);
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
