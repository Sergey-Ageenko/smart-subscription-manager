package com.ssm.auth_service.service;

import com.ssm.auth_service.model.entities.OutboxEvent;

import java.util.List;

public interface OutboxService {
    List<OutboxEvent> claim();
    void markSent(OutboxEvent event);
    void markProcessing(OutboxEvent event);
    void markFailed(OutboxEvent event);
}
