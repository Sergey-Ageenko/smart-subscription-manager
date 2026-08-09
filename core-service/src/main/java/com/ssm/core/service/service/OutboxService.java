package com.ssm.core.service.service;

import com.ssm.core.service.model.entity.OutboxEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboxService {
    List<OutboxEvent> claim();
    void markSent(OutboxEvent event);
    void markFailed(OutboxEvent event);
    int recoverStuckEvents(LocalDateTime localDateTime);
}
