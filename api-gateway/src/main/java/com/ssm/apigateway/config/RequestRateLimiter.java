package com.ssm.apigateway.config;

import com.ssm.apigateway.constant.ApiConstants;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class RequestRateLimiter {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10, 20);
    }

    @Bean
    public KeyResolver keyResolver() {
        return exchange -> {
            String userId = exchange.getRequest()
                    .getHeaders()
                    .getFirst(ApiConstants.USER_ID);
            if (userId != null){
                return Mono.just(userId);
            }
            return Mono.just(
                    Objects.requireNonNull(exchange.getRequest()
                                    .getRemoteAddress())
                            .getAddress()
                            .getHostAddress()
            );
        };
    }
}
