package com.ssm.auth_service.service.impl;

import com.ssm.auth_service.model.entity.OutboxEvent;
import com.ssm.auth_service.model.enums.OutboxStatus;
import com.ssm.auth_service.repository.OutboxRepository;
import com.ssm.auth_service.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {

    private final OutboxRepository outboxRepository;

    @Override
    @Transactional
    public List<OutboxEvent> claim() {
        return outboxRepository.findReady();
    }

    @Override
    @Transactional
    public void markSent(OutboxEvent event) {
        event.setSentAt(LocalDateTime.now());
        event.setStatus(OutboxStatus.SENT);
        outboxRepository.save(event);
    }

    @Override
    @Transactional
    public void markFailed(OutboxEvent event) {
        event.setRetryCount(event.getRetryCount() + 1);

        if (event.getRetryCount() >= 5) {
            event.setStatus(OutboxStatus.FAILED);
        } else {
            event.setStatus(OutboxStatus.NEW);
        }

        outboxRepository.save(event);
    }

    @Override
    @Transactional
    public int recoverStuckEvents(LocalDateTime threshold) {
        List<OutboxEvent> recovered = outboxRepository.findStuckProcessingEvents(threshold);
        return recovered.size();
    }
}
