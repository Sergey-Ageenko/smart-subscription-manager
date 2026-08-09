package com.ssm.billing.service.service;


import com.ssm.billing.service.model.entity.ProcessedEvent;

public interface ProcessedEventService {

    void process(ProcessedEvent event);
}
