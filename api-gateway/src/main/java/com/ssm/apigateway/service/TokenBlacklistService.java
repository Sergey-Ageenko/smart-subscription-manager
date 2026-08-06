package com.ssm.apigateway.service;

import reactor.core.publisher.Mono;

public interface TokenBlacklistService {
    Mono<Boolean> isBlacklisted(String token);
}
