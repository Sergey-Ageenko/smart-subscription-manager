package com.ssm.core.service.kafka.producer.impl;

import com.ssm.core.service.kafka.producer.OutboxPublisher;
import com.ssm.core.service.mapper.AvroEventMapperRegistry;
import com.ssm.core.service.model.entity.OutboxEvent;
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
