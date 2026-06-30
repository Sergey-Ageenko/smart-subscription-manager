package com.ssm.auth_service.kafka.impl;

import com.ssm.auth_service.kafka.OutboxPublisher;
import com.ssm.auth_service.kafka.OutboxScheduler;
import com.ssm.auth_service.model.entities.OutboxEvent;
import com.ssm.auth_service.model.enums.OutboxStatus;
import com.ssm.auth_service.repositories.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaOutboxScheduler implements OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final OutboxPublisher outboxPublisher;

    @Override
    @Scheduled(fixedDelayString = "${app.outbox.poll-interval-ms}")
    @Transactional
    public void process() {
        List<OutboxEvent> events = outboxRepository.findReady();
        if (events.isEmpty()) {
            return;
        }
        for (OutboxEvent event : events) {
            try {
                outboxPublisher.publish(event);
                markSent(event);
            } catch (Exception e) {
                log.error("Outbox publish failed id={}", event.getId(), e);
            }
        }
    }

    private void markSent(OutboxEvent event) {
        event.setSentAt(LocalDateTime.now());
        event.setStatus(OutboxStatus.SENT);
        outboxRepository.save(event);
    }
}
