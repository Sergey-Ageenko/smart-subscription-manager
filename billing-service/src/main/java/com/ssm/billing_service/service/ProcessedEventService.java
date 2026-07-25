package com.ssm.billing_service.service;


import com.ssm.billing_service.model.entity.ProcessedEvent;

public interface ProcessedEventService {

    void process(ProcessedEvent event);
}
