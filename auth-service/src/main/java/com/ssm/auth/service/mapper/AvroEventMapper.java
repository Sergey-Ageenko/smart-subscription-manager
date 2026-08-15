package com.ssm.auth.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface AvroEventMapper {
    String eventName();
    Object toAvro(String payload) throws JsonProcessingException;
}
