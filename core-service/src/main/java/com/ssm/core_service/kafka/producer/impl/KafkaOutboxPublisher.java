package com.ssm.core_service.kafka.producer.impl;

import com.ssm.core_service.kafka.producer.OutboxPublisher;
import com.ssm.core_service.model.entity.OutboxEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaOutboxPublisher implements OutboxPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publish(OutboxEvent event) {
        try {
            kafkaTemplate.send(
                    event.getEventName(),
                    event.getEventId().toString(),
                    event.getPayload()
            ).whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Kafka publish failed eventId={}", event.getEventId(), ex);
                } else {
                    log.debug("Kafka publish success eventId={}", event.getEventId());
                }
            });
        } catch (Exception e) {
            throw new IllegalStateException("Kafka publish failed", e);
        }
    }
}

