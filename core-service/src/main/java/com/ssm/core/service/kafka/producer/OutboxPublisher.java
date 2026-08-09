package com.ssm.core.service.kafka.producer;

import com.ssm.core.service.model.entity.OutboxEvent;

public interface OutboxPublisher {
    void publish(OutboxEvent event);
}
