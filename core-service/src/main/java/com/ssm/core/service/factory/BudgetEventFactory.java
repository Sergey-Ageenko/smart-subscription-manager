package com.ssm.core.service.factory;

import com.ssm.core.service.model.constant.ApiConstants;
import com.ssm.core.service.model.dto.BudgetSettingsUpdatedDto;
import com.ssm.core.service.model.entity.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BudgetEventFactory {

    private final OutboxEventFactory outboxFactory;

    public OutboxEvent updated(UUID userId) {
        BudgetSettingsUpdatedDto dto = new BudgetSettingsUpdatedDto(userId);
        return outboxFactory.create(
                ApiConstants.BUDGET_UPDATED,
                dto
        );
    }
}
