package com.ssm.auth_service.kafka.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.auth_service.kafka.OutboxPublisher;
import com.ssm.auth_service.model.constants.ApiConstants;
import com.ssm.auth_service.model.entities.OutboxEvent;
import events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class KafkaOutboxPublisher implements OutboxPublisher {

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(OutboxEvent event) {
        try {
            UserRegisteredEvent payload =
                    objectMapper.readValue(event.getPayload(), UserRegisteredEvent.class);
            kafkaTemplate.send(
                    ApiConstants.USER_REGISTERED,
                    payload.userId().toString(),
                    payload
            ).get();
        } catch (Exception e) {
            throw new IllegalStateException("Kafka publish failed", e);
        }
    }
}

