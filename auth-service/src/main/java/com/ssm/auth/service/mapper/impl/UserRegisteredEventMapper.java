package com.ssm.auth.service.mapper.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.auth.service.mapper.AvroEventMapper;
import com.ssm.auth.service.model.constant.ApiConstants;
import com.ssm.auth.service.model.constant.ApiErrorMessage;
import com.ssm.auth.service.model.dto.UserRegisteredDto;
import com.ssm.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisteredEventMapper implements AvroEventMapper {

    private final ObjectMapper objectMapper;

    @Override
    public String eventName() {
        return ApiConstants.USER_REGISTERED;
    }

    @Override
    public Object toAvro(String payload) {
        try {
            UserRegisteredDto dto = objectMapper.readValue(
                    payload,
                    UserRegisteredDto.class);
            return UserRegisteredEvent.newBuilder()
                    .setUserId(dto.userId())
                    .setFirstName(dto.firstName())
                    .setLastName(dto.lastName())
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    ApiErrorMessage.INVALID_PAYLOAD_EVENT.getMessage(eventName())
            );
        }
    }
}
