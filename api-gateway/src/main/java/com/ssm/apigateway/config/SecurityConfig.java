package com.ssm.apigateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.apigateway.config.jwtManager.JwtReactiveAuthenticationManager;
import com.ssm.apigateway.config.jwtManager.JwtServerAuthenticationConverter;
import com.ssm.apigateway.security.handler.CustomAccessDeniedHandler;
import com.ssm.apigateway.security.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationWebFilter jwtAuthenticationWebFilter) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper))
                        .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper))
                )
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterAt(jwtAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }

    @Bean
    public AuthenticationWebFilter jwtAuthenticationWebFilter(JwtReactiveAuthenticationManager authManager, JwtServerAuthenticationConverter converter, CustomAuthenticationEntryPoint entryPoint) {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authManager);
        filter.setServerAuthenticationConverter(converter);
        filter.setRequiresAuthenticationMatcher(
                exchange -> {
                    String path = exchange.getRequest().getPath().value();
                    if (path.startsWith("/api/v1/auth")){
                        return ServerWebExchangeMatcher.MatchResult.notMatch();
                    }
                    return ServerWebExchangeMatcher.MatchResult.match();
                }
        );
        filter.setAuthenticationFailureHandler(
                (webFilterExchange, exception) ->
                        entryPoint.commence(
                                webFilterExchange.getExchange(),
                                exception
                        )
        );
        return filter;
    }
}

