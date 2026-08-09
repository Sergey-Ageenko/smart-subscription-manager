package com.ssm.auth.service.model.response;

import java.util.List;
import java.util.UUID;

public record TokenValidationResponse(
        UUID userId,
        List<String> roles
) {
}
