package com.ssm.core.service.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.core.service.mapper.AvroEventMapper;
import com.ssm.core.service.model.constant.ApiConstants;
import com.ssm.core.service.model.constant.ApiErrorMessage;
import com.ssm.core.service.model.dto.SubscriptionCancelledDto;
import com.ssm.events.SubscriptionCancelledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionCancelledEventMapper implements AvroEventMapper {

    private final ObjectMapper objectMapper;

    @Override
    public String eventName() {
        return ApiConstants.SUBSCRIPTION_CANCELLED;
    }

    @Override
    public Object toAvro(String payload) {
        try {
            SubscriptionCancelledDto dto = objectMapper.readValue(
                    payload,
                    SubscriptionCancelledDto.class);
            return SubscriptionCancelledEvent.newBuilder()
                    .setUserId(dto.userId())
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    ApiErrorMessage.INVALID_PAYLOAD_EVENT.getMessage(eventName())
            );
        }
    }
}
