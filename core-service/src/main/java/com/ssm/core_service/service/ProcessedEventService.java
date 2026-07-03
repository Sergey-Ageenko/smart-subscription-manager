package com.ssm.core_service.service;

import com.ssm.core_service.model.entity.ProcessedEvent;

public interface ProcessedEventService {

    void process(ProcessedEvent event);
}
