package com.ssm.core.service.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.core.service.mapper.AvroEventMapper;
import com.ssm.core.service.model.constant.ApiConstants;
import com.ssm.core.service.model.constant.ApiErrorMessage;
import com.ssm.core.service.model.dto.SubscriptionCreatedDto;
import com.ssm.events.SubscriptionCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionCreatedEventMapper implements AvroEventMapper {

    private final ObjectMapper objectMapper;

    @Override
    public String eventName() {
        return ApiConstants.SUBSCRIPTION_CREATED;
    }

    @Override
    public Object toAvro(String payload) {
        try {
            SubscriptionCreatedDto dto = objectMapper.readValue(
                    payload,
                    SubscriptionCreatedDto.class);
            return SubscriptionCreatedEvent.newBuilder()
                    .setUserId(dto.userId())
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    ApiErrorMessage.INVALID_PAYLOAD_EVENT.getMessage(eventName())
            );
        }
    }
}
