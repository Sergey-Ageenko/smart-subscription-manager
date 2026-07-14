package com.ssm.auth_service.factory;

import com.ssm.auth_service.model.constant.ApiConstants;
import com.ssm.auth_service.model.entity.OutboxEvent;
import com.ssm.auth_service.model.request.RegisterRequest;
import com.ssm.common.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserEventFactory {

    private final OutboxEventFactory outboxFactory;

    public OutboxEvent registered(UUID userId, RegisterRequest request) {
        UserRegisteredEvent event =
                new UserRegisteredEvent(
                        userId,
                        request.getFirstName(),
                        request.getLastName()
                );
        return outboxFactory.create(
                ApiConstants.USER_REGISTERED,
                event
        );
    }

}
