package com.ssm.core_service.kafka.producer;

import com.ssm.core_service.model.entity.OutboxEvent;

public interface OutboxPublisher {
    void publish(OutboxEvent event);
}
