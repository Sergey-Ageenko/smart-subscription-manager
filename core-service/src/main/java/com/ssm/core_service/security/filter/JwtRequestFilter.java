package com.ssm.core_service.security.filter;


import com.ssm.core_service.model.constants.ApiErrorMessage;
import com.ssm.core_service.model.constants.ApiConstants;
import com.ssm.core_service.security.JwtTokenProvider;
import com.ssm.core_service.security.JwtUserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain)
            throws ServletException, IOException {


        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            Claims claims = jwtTokenProvider.parse(token);

            if (!jwtTokenProvider.isValid(claims)) {
                filterChain.doFilter(request, response);
                return;
            }

            UUID userId = UUID.fromString(jwtTokenProvider.getUserId(claims));

            List<SimpleGrantedAuthority> authorities =
                    jwtTokenProvider.getRoles(claims).stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

            Authentication auth =
                    new UsernamePasswordAuthenticationToken(
                            new JwtUserPrincipal(userId),
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (JwtException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            request.setAttribute(ApiConstants.JWT_ERROR, ApiErrorMessage.INVALID_TOKEN);
        }

        filterChain.doFilter(request, response);
    }
}