package com.ssm.apigateway.service.impl;

import com.ssm.apigateway.constant.ApiConstants;
import com.ssm.apigateway.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @Override
    public Mono<Boolean> isBlacklisted(String token) {
        return reactiveRedisTemplate.hasKey(
                ApiConstants.PREFIX_BLACKLIST + token
        );
    }
}
