package com.ssm.billing_service.security.filter;

import com.ssm.billing_service.model.constant.ApiConstants;
import com.ssm.billing_service.model.constant.ApiErrorMessage;
import com.ssm.billing_service.security.UserPrincipal;
import com.ssm.common.exception.UnauthorizedException;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        UUID userId = UUID.fromString(request.getHeader(ApiConstants.USER_ID));
        String roles = request.getHeader(ApiConstants.USER_ROLES);
        try {
            List<SimpleGrantedAuthority> authorities = Arrays.stream(roles.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            Authentication auth =
                    new UsernamePasswordAuthenticationToken(
                            new UserPrincipal(userId),
                            null,
                            authorities
                    );
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException(ApiErrorMessage.INVALID_GATEWAY_AUTHENTICATION.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}