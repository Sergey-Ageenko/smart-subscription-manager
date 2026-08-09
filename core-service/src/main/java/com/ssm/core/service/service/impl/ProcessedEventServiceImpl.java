package com.ssm.core.service.service.impl;

import com.ssm.common.exception.DuplicateEventException;
import com.ssm.core.service.model.entity.ProcessedEvent;
import com.ssm.core.service.repository.ProcessedEventRepository;
import com.ssm.core.service.service.ProcessedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessedEventServiceImpl implements ProcessedEventService {

    private final ProcessedEventRepository processedEventRepository;

    @Override
    @Transactional
    public void process(ProcessedEvent event) {
        try {
            processedEventRepository.saveAndFlush(event);
            log.debug("Event persisted with Id: {}", event.getEventId());
        } catch (DataIntegrityViolationException e) {
            log.warn("Event already processed: {}", event.getEventId());
            throw new DuplicateEventException(event.getEventId().toString());
        }
    }
}
