package com.ssm.billing.service.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.billing.service.model.constant.ApiConstants;
import com.ssm.billing.service.model.entity.ProcessedEvent;
import com.ssm.billing.service.service.ForecastService;
import com.ssm.billing.service.service.ProcessedEventService;
import com.ssm.common.event.BudgetSettingsUpdatedEvent;
import com.ssm.common.exception.DuplicateEventException;
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
public class BudgetSettingsUpdatedEventListener {

    private final ProcessedEventService processedEventService;
    private final ObjectMapper objectMapper;
    private final ForecastService forecastService;

    @KafkaListener(
            topics = ApiConstants.BUDGET_UPDATED,
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
            BudgetSettingsUpdatedEvent event =
                    objectMapper.readValue(record.value(), BudgetSettingsUpdatedEvent.class);
            processedEventService.process(ProcessedEvent.builder()
                    .eventId(UUID.fromString(record.key()))
                    .processedAt(LocalDateTime.now())
                    .build());
            forecastService.calculateForecast(event.userId());
        } catch (DuplicateEventException e) {
            log.debug("Duplicate message {}", record.key());

        }
    }
}
