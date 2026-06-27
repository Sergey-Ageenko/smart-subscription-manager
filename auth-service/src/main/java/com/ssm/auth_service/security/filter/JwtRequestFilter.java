package com.ssm.auth_service.security.filter;

import com.ssm.auth_service.model.constants.ApiErrorMessage;
import com.ssm.auth_service.model.constants.ApiConstants;
import com.ssm.auth_service.security.JwtTokenProvider;
import com.ssm.auth_service.security.JwtUserPrincipal;
import com.ssm.auth_service.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

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
            String userId = jwtTokenProvider.extractUserId(token);
            if (StringUtils.isNotEmpty(userId) && SecurityContextHolder
                    .getContext().getAuthentication() == null) {
                JwtUserPrincipal jwtUserPrincipal = userService.loadUserById(userId);
                if (jwtTokenProvider.isTokenValid(token, jwtUserPrincipal)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            jwtUserPrincipal,
                            null,
                            jwtUserPrincipal.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
        } catch (JwtException ex) {
            SecurityContextHolder.clearContext();
            request.setAttribute(ApiConstants.JWT_ERROR, ApiErrorMessage.INVALID_TOKEN.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}