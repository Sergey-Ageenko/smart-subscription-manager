package com.ssm.core.service.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.core.service.mapper.AvroEventMapper;
import com.ssm.core.service.model.constant.ApiConstants;
import com.ssm.core.service.model.constant.ApiErrorMessage;
import com.ssm.core.service.model.dto.SubscriptionActivatedDto;
import com.ssm.events.SubscriptionActivatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionActivatedEventMapper implements AvroEventMapper {

    private final ObjectMapper objectMapper;

    @Override
    public String eventName() {
        return ApiConstants.SUBSCRIPTION_ACTIVATED;
    }

    @Override
    public Object toAvro(String payload) {
        try {
            SubscriptionActivatedDto dto = objectMapper.readValue(
                    payload,
                    SubscriptionActivatedDto.class);
            return SubscriptionActivatedEvent.newBuilder()
                    .setUserId(dto.userId())
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    ApiErrorMessage.INVALID_PAYLOAD_EVENT.getMessage(eventName())
            );
        }
    }
}
