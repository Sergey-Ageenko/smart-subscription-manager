package com.ssm.core_service.kafka.listener;

import com.ssm.core_service.model.constants.ApiConstants;
import com.ssm.core_service.service.ProfileService;
import events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegisteredEventListener {

    private final ProfileService profileService;

    @KafkaListener(
            topics = ApiConstants.USER_REGISTERED,
            groupId = "core-group"
    )
    public void handle(ConsumerRecord<String, UserRegisteredEvent> record) {
        try {
            log.info("RECEIVED key={}, offset={}",
                    record.key(),
                    record.offset()
            );
            profileService.createProfile(record.value());
        } catch (Exception e) {
            log.error("Processing failed offset={}", record.offset(), e);
            throw e;
        }
    }
}
