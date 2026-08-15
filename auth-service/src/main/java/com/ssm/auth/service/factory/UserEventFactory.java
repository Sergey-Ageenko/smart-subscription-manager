package com.ssm.auth.service.factory;

import com.ssm.auth.service.model.constant.ApiConstants;
import com.ssm.auth.service.model.dto.UserRegisteredDto;
import com.ssm.auth.service.model.entity.OutboxEvent;
import com.ssm.auth.service.model.request.RegisterRequest;
import com.ssm.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserEventFactory {

    private final OutboxEventFactory outboxFactory;

    public OutboxEvent registered(UUID userId, RegisterRequest request) {
        UserRegisteredDto dto = new UserRegisteredDto(
                userId,
                request.getFirstName(),
                request.getLastName()
        );
        return outboxFactory.create(
                ApiConstants.USER_REGISTERED,
                dto
        );
    }

}
