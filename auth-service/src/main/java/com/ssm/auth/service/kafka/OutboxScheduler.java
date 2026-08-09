package com.ssm.auth.service.kafka;

public interface OutboxScheduler {
    void process();
    void recover();
}
