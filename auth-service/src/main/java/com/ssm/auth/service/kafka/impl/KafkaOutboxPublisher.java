package com.ssm.auth.service.kafka.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.auth.service.kafka.OutboxPublisher;
import com.ssm.auth.service.mapper.AvroEventMapperRegistry;
import com.ssm.auth.service.model.constant.ApiConstants;
import com.ssm.auth.service.model.dto.UserRegisteredDto;
import com.ssm.auth.service.model.entity.OutboxEvent;
import com.ssm.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaOutboxPublisher implements OutboxPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AvroEventMapperRegistry avroEventMapperRegistry;

    @Override
    public void publish(OutboxEvent event) {
        try {
            Object payload = avroEventMapperRegistry.toAvro(event);
            kafkaTemplate.send(
                    event.getEventName(),
                    event.getEventId().toString(),
                    payload
            ).get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new IllegalStateException("Kafka publish failed", e);
        }
    }
}

