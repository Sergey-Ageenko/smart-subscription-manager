package com.ssm.auth_service.service;

import com.ssm.auth_service.model.entity.OutboxEvent;

import java.util.List;

public interface OutboxService {
    List<OutboxEvent> claim();
    void markSent(OutboxEvent event);
    void markFailed(OutboxEvent event);
}
