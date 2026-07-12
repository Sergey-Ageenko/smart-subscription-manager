package com.ssm.core_service.factory;

import com.ssm.core_service.model.constant.ApiConstants;
import com.ssm.core_service.model.entity.OutboxEvent;
import com.ssm.core_service.model.entity.profileSubscription.ProfileSubscription;
import com.ssm.events.SubscriptionCancelledEvent;
import com.ssm.events.SubscriptionCreatedEvent;
import com.ssm.events.SubscriptionUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionEventFactory {

    private final OutboxEventFactory outboxFactory;

    public OutboxEvent created(ProfileSubscription subscription) {
        SubscriptionCreatedEvent event =
                new SubscriptionCreatedEvent(
                        subscription.getSubscription().getId(),
                        subscription.getPrice(),
                        subscription.getBillingPeriod().name()
                );
        return outboxFactory.create(
                ApiConstants.SUBSCRIPTION_CREATED,
                event
        );
    }

    public OutboxEvent updated(ProfileSubscription subscription) {
        SubscriptionUpdatedEvent event =
                new SubscriptionUpdatedEvent(
                        subscription.getSubscription().getId(),
                        subscription.getPrice(),
                        subscription.getBillingPeriod().name()
                );
        return outboxFactory.create(
                ApiConstants.SUBSCRIPTION_UPDATED,
                event
        );
    }

    public OutboxEvent cancelled(ProfileSubscription subscription) {
        SubscriptionCancelledEvent event =
                new SubscriptionCancelledEvent(
                        subscription.getSubscription().getId(),
                        subscription.getStatus().name()
                );
        return outboxFactory.create(
                ApiConstants.SUBSCRIPTION_UPDATED,
                event
        );
    }
}
