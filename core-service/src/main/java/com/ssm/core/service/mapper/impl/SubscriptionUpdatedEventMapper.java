package com.ssm.core.service.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.core.service.mapper.AvroEventMapper;
import com.ssm.core.service.model.constant.ApiConstants;
import com.ssm.core.service.model.constant.ApiErrorMessage;
import com.ssm.core.service.model.dto.SubscriptionUpdatedDto;
import com.ssm.events.SubscriptionUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionUpdatedEventMapper implements AvroEventMapper {

    private final ObjectMapper objectMapper;

    @Override
    public String eventName() {
        return ApiConstants.SUBSCRIPTION_UPDATED;
    }

    @Override
    public Object toAvro(String payload) {
        try {
            SubscriptionUpdatedDto dto = objectMapper.readValue(
                    payload,
                    SubscriptionUpdatedDto.class);
            return SubscriptionUpdatedEvent.newBuilder()
                    .setUserId(dto.userId())
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    ApiErrorMessage.INVALID_PAYLOAD_EVENT.getMessage(eventName())
            );
        }
    }
}
