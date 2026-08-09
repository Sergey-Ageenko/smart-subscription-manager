package com.ssm.auth.service.kafka;

import com.ssm.auth.service.model.entity.OutboxEvent;

public interface OutboxPublisher {
    void publish(OutboxEvent event);
}
