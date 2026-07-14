package com.ssm.apigateway.config;

import com.ssm.apigateway.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.info("JWT AUTH MANAGER CALLED");
        String token = authentication.getCredentials().toString();
        try {
            Claims claims = jwtTokenProvider.extractAllClaims(token);
            return Mono.just(new UsernamePasswordAuthenticationToken(
                    jwtTokenProvider.getUserId(claims),
                    null,
                    jwtTokenProvider.getRoles(claims)
            ));
        }catch (JwtException e){
            return Mono.error(new BadCredentialsException("Invalid JWT token"));
        }
    }
}
