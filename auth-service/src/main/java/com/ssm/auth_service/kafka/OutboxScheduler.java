package com.ssm.auth_service.kafka;

public interface OutboxScheduler {
    void process();
}
