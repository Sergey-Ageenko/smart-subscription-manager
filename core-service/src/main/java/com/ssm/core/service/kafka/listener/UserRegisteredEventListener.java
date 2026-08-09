package com.ssm.core.service.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.common.exception.DuplicateEventException;
import com.ssm.core.service.model.constant.ApiConstants;
import com.ssm.core.service.model.entity.ProcessedEvent;
import com.ssm.core.service.service.ProcessedEventService;
import com.ssm.core.service.service.ProfileService;
import com.ssm.common.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegisteredEventListener {

    private final ProfileService profileService;
    private final ProcessedEventService processedEventService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = ApiConstants.USER_REGISTERED,
            groupId = "core-group"
    )
    @Transactional
    public void handle(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.debug("Received message - eventId: {} - key: {} - payload: {}",
                record.value(),
                record.key(),
                record.value()
        );
        try {
            UserRegisteredEvent event =
                    objectMapper.readValue(record.value(), UserRegisteredEvent.class);
            processedEventService.process(ProcessedEvent.builder()
                    .eventId(UUID.fromString(record.key()))
                    .processedAt(LocalDateTime.now())
                    .build());
            profileService.createProfile(event);
        } catch (DuplicateEventException e) {
            log.debug("Duplicate message {}", record.key());
        }
    }
}
