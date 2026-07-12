package com.ssm.core_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.core_service.exception.NotFoundException;
import com.ssm.core_service.model.constant.ApiConstants;
import com.ssm.core_service.model.constant.ApiErrorMessage;
import com.ssm.core_service.model.entity.Budget;
import com.ssm.core_service.model.entity.OutboxEvent;
import com.ssm.core_service.model.enums.OutboxStatus;
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

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;


    @Override
    @Transactional(readOnly = true)
    public CoreResponse<BudgetResponse> getBudget(UUID profileId) {
        Budget budget = budgetRepository.findByProfile_Id(profileId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_BUDGET_NOT_FOUND_BY_ID.getMessage(profileId)
                ));
        return CoreResponse.createSuccessful(
                toResponse(budget)
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
        BudgetSettingsUpdatedEvent event = new BudgetSettingsUpdatedEvent(
                UUID.randomUUID(),
                budget.getId(),
                budget.getMonthlyLimit()
        );
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .eventName(ApiConstants.BUDGET_UPDATED)
                .eventId(event.eventId())
                .payload(objectMapper.writeValueAsString(event))
                .status(OutboxStatus.NEW)
                .createdAt(LocalDateTime.now())
                .build();
        outboxRepository.save(outboxEvent);
        log.info("Budget {} updated successfully.", budget.getId());
        return CoreResponse.createSuccessful(
                toResponse(budget)
        );
    }

    private BudgetResponse toResponse(Budget budget) {
        return new BudgetResponse(
                budget.getMonthlyLimit()
        );
    }
}
