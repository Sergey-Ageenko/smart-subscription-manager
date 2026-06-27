package com.ssm.auth_service.kafka.producer;


import events.UserRegisteredEvent;

public interface UserProducer {
    void send(UserRegisteredEvent event);
}
