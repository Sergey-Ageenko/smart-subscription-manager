package com.ssm.core.service.factory;

import com.ssm.core.service.model.constant.ApiConstants;
import com.ssm.core.service.model.dto.*;
import com.ssm.core.service.model.entity.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SubscriptionEventFactory {

    private final OutboxEventFactory outboxFactory;

    public OutboxEvent created(UUID userId) {
        SubscriptionCreatedDto dto = new SubscriptionCreatedDto(userId);
        return outboxFactory.create(
                ApiConstants.SUBSCRIPTION_CREATED,
                dto
        );
    }

    public OutboxEvent updated(UUID userId) {
        SubscriptionUpdatedDto dto = new SubscriptionUpdatedDto(userId);
        return outboxFactory.create(
                ApiConstants.SUBSCRIPTION_UPDATED,
                dto
        );
    }

    public OutboxEvent cancelled(UUID userId) {
        SubscriptionCancelledDto dto = new SubscriptionCancelledDto(userId);
        return outboxFactory.create(
                ApiConstants.SUBSCRIPTION_CANCELLED,
                dto
        );
    }

    public OutboxEvent activated(UUID userId) {
        SubscriptionActivatedDto dto = new SubscriptionActivatedDto(userId);
        return outboxFactory.create(
                ApiConstants.SUBSCRIPTION_ACTIVATED,
                dto
        );
    }

    public OutboxEvent deleted(UUID userId) {
        SubscriptionDeletedDto dto = new SubscriptionDeletedDto(userId);
        return outboxFactory.create(
                ApiConstants.SUBSCRIPTION_DELETED,
                dto
        );
    }
}
