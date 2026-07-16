package com.ssm.core_service.factory;

import com.ssm.core_service.model.constant.ApiConstants;
import com.ssm.core_service.model.entity.Budget;
import com.ssm.core_service.model.entity.OutboxEvent;
import com.ssm.common.event.BudgetSettingsUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BudgetEventFactory {

    private final OutboxEventFactory outboxFactory;

    public OutboxEvent updated(Budget budget) {
        BudgetSettingsUpdatedEvent event = new BudgetSettingsUpdatedEvent(
                budget.getId(),
                budget.getMonthlyLimit()
        );
        return outboxFactory.create(
                ApiConstants.BUDGET_UPDATED,
                event
        );
    }
}
