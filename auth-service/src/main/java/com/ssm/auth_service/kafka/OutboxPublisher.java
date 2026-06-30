package com.ssm.auth_service.kafka;

import com.ssm.auth_service.model.entities.OutboxEvent;

public interface OutboxPublisher {
    void publish(OutboxEvent event);
}
