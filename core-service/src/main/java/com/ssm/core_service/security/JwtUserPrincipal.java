package com.ssm.core_service.security;

import java.util.UUID;


public record JwtUserPrincipal(
        UUID userId
) {
}
