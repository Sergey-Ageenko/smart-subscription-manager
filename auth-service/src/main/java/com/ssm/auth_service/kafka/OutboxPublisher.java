package com.ssm.auth_service.kafka;

import com.ssm.auth_service.model.entity.OutboxEvent;

public interface OutboxPublisher {
    void publish(OutboxEvent event);
}
