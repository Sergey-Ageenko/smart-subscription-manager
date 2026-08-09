package com.ssm.core.service.kafka.producer;

public interface OutboxScheduler {
    void process();
    void recover();
}
