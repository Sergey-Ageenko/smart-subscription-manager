package com.ssm.core_service.kafka.producer;

public interface OutboxScheduler {
    void process();
    void recover();
}
