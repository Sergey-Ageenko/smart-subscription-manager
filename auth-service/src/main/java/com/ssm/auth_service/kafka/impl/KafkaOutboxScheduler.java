package com.ssm.auth_service.kafka.impl;

import com.ssm.auth_service.kafka.OutboxPublisher;
import com.ssm.auth_service.kafka.OutboxScheduler;
import com.ssm.auth_service.model.entities.OutboxEvent;
import com.ssm.auth_service.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaOutboxScheduler implements OutboxScheduler {

    private final OutboxService outboxService;
    private final OutboxPublisher outboxPublisher;

    @Override
    @Scheduled(fixedDelayString = "${app.outbox.poll-interval-ms}")
    public void process() {
        List<OutboxEvent> events = outboxService.claim();
        if (events.isEmpty()) {
            return;
        }
        for (OutboxEvent event : events) {
            try {
                outboxService.markProcessing(event);
                outboxPublisher.publish(event);
                outboxService.markSent(event);
            } catch (Exception e) {
                log.error("Outbox publish failed id={}", event.getId(), e);
                outboxService.markFailed(event);
            }
        }
    }
}
