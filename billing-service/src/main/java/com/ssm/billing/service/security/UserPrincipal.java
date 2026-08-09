package com.ssm.billing.service.security;

import java.util.UUID;


public record UserPrincipal(
        UUID userId
) {
}
