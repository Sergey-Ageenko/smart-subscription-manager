package com.ssm.core.service.service;

import com.ssm.core.service.model.entity.ProcessedEvent;

public interface ProcessedEventService {

    void process(ProcessedEvent event);
}
