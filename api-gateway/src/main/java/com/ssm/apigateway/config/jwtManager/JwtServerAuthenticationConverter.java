package com.ssm.apigateway.config.jwtManager;

import com.ssm.apigateway.constant.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

    private static final int tokenBeginIndex = 7;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        log.info("JWT CONVERTER CALLED");
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(ApiConstants.Bearer))
                .map(authHeader -> authHeader.substring(tokenBeginIndex))
                .map(token -> new UsernamePasswordAuthenticationToken(null, token));
    }
}
