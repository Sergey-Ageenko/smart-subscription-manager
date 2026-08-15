package com.ssm.auth.service.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssm.auth.service.model.entity.OutboxEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AvroEventMapperRegistry {

    private final Map<String, AvroEventMapper> mappers;

    public AvroEventMapperRegistry(List<AvroEventMapper> mappers) {
        this.mappers = mappers.stream()
                .collect(Collectors.toMap(
                        AvroEventMapper::eventName,
                        Function.identity()
                ));
    }

    public Object toAvro(OutboxEvent event) throws JsonProcessingException {
        AvroEventMapper mapper = mappers.get(event.getEventName());
        return mapper.toAvro(event.getPayload());
    }
}
