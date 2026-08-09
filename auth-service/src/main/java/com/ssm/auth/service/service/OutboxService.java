package com.ssm.auth.service.service;

import com.ssm.auth.service.model.entity.OutboxEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboxService {
    List<OutboxEvent> claim();
    void markSent(OutboxEvent event);
    void markFailed(OutboxEvent event);
    int recoverStuckEvents(LocalDateTime localDateTime);
}
