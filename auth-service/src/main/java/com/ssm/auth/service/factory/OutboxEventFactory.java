package com.ssm.auth.service.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.auth.service.model.entity.OutboxEvent;
import com.ssm.auth.service.model.enums.OutboxStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutboxEventFactory {

    private final ObjectMapper objectMapper;


    public OutboxEvent create(String eventName, Object event) {
        try {
            return OutboxEvent.builder()
                    .eventId(UUID.randomUUID())
                    .eventName(eventName)
                    .payload(objectMapper.writeValueAsString(event))
                    .status(OutboxStatus.NEW)
                    .createdAt(LocalDateTime.now())
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}