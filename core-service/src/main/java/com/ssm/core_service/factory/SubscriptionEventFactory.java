package com.ssm.core_service.factory;

import com.ssm.common.event.*;
import com.ssm.core_service.model.constant.ApiConstants;
import com.ssm.core_service.model.entity.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SubscriptionEventFactory {

    private final OutboxEventFactory outboxFactory;

    public OutboxEvent created(UUID userId) {
        SubscriptionCreatedEvent event =
                new SubscriptionCreatedEvent(
                        userId, true
                );
        return outboxFactory.create(
                ApiConstants.SUBSCRIPTION_CREATED,
                event
        );
    }

    public OutboxEvent updated(UUID userId) {
        SubscriptionUpdatedEvent event =
                new SubscriptionUpdatedEvent(
                        userId, true
                );
        return outboxFactory.create(
                ApiConstants.SUBSCRIPTION_UPDATED,
                event
        );
    }

    public OutboxEvent cancelled(UUID userId) {
        SubscriptionCancelledEvent event =
                new SubscriptionCancelledEvent(
                        userId, true
                );
        return outboxFactory.create(
                ApiConstants.SUBSCRIPTION_CANCELLED,
                event
        );
    }

    public OutboxEvent activated(UUID userId) {
        SubscriptionActivatedEvent event =
                new SubscriptionActivatedEvent(
                        userId, true
                );
        return outboxFactory.create(
                ApiConstants.SUBSCRIPTION_ACTIVATED,
                event
        );
    }

    public OutboxEvent deleted(UUID userId) {
        SubscriptionDeletedEvent event =
                new SubscriptionDeletedEvent(
                        userId, true
                );
        return outboxFactory.create(
                ApiConstants.SUBSCRIPTION_DELETED,
                event
        );
    }
}
