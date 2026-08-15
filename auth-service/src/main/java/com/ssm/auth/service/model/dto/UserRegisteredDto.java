package com.ssm.auth.service.model.dto;

import java.util.UUID;

public record UserRegisteredDto (
        UUID userId,
        String firstName,
        String lastName
){
}
