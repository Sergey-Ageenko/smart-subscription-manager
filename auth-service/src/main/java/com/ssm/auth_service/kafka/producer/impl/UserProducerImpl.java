package com.ssm.auth_service.kafka.producer.impl;

import com.ssm.auth_service.kafka.producer.UserProducer;
import com.ssm.auth_service.model.constants.ApiConstants;
import events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProducerImpl implements UserProducer {

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    @Override
    public void send(UserRegisteredEvent event) {
        kafkaTemplate.send(
                        ApiConstants.USER_REGISTERED_TOPIC,
                        event.userId().toString(),
                        event)
                .whenComplete((result, ex) ->
                        {
                            if (ex != null) {
                                log.error("Failed to send event to Kafka", ex);
                            } else {
                                log.info(
                                        "UserRegisteredEvent sent. userId={}, topic={}, partition={}, offset={}",
                                        event.userId(),
                                        result.getRecordMetadata().topic(),
                                        result.getRecordMetadata().partition(),
                                        result.getRecordMetadata().offset()
                                );
                            }
                        }
                );
    }
}
