package com.ssm.apigateway.security.filter;

import com.ssm.apigateway.constant.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("GLOBAL FILTER {}", exchange.getRequest().getURI());
        return exchange.getPrincipal()
                .cast(Authentication.class)
                .flatMap(authentication -> {
                    String userId = authentication.getName();
                    String roles = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(","));
                    ServerHttpRequest request = exchange.getRequest()
                            .mutate()
                            .header(ApiConstants.USER_ID, userId)
                            .header(ApiConstants.USER_ROLES, roles)
                            .build();
                    log.info("Forwarding userId={}", userId);
                    log.info("Forwarding roles={}", roles);
                    return chain.filter(exchange
                            .mutate()
                            .request(request)
                            .build());
                })
                .switchIfEmpty(
                        Mono.defer(() -> chain.filter(exchange))
                );
    }

    @Override
    public int getOrder() {
        return 900;
    }
}
