package com.ssm.billing_service.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.billing_service.model.constant.ApiConstants;
import com.ssm.billing_service.model.entity.ProcessedEvent;
import com.ssm.billing_service.service.ForecastService;
import com.ssm.billing_service.service.ProcessedEventService;
import com.ssm.common.event.SubscriptionActivatedEvent;
import com.ssm.common.event.SubscriptionCancelledEvent;
import com.ssm.common.exception.DuplicateEventException;
import com.ssm.common.exception.RetryableException;
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
public class SubscriptionActivatedEventListener {

    private final ProcessedEventService processedEventService;
    private final ObjectMapper objectMapper;
    private final ForecastService forecastService;

    @KafkaListener(
            topics = ApiConstants.SUBSCRIPTION_ACTIVATED,
            groupId = "billing-group"
    )
    @Transactional
    public void handle(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.debug("Received message - eventId: {} - key: {} - payload: {}",
                record.value(),
                record.key(),
                record.value()
        );
        try {
            SubscriptionActivatedEvent event =
                    objectMapper.readValue(record.value(), SubscriptionActivatedEvent.class);
            processedEventService.process(ProcessedEvent.builder()
                    .eventId(UUID.fromString(record.key()))
                    .processedAt(LocalDateTime.now())
                    .build());
            forecastService.calculateForecast(event.userId());
        } catch (DuplicateEventException e) {
            log.debug("Duplicate message {}", record.key());

        } catch (RetryableException e) {
            log.warn("Retryable error", e);
            throw e;

        } catch (Exception e) {
            log.error("Unexpected error", e);
            throw e;
        }
    }
}
