package com.ssm.billing_service.security;

import java.util.UUID;


public record UserPrincipal(
        UUID userId
) {
}
