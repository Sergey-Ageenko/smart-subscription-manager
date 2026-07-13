package com.ssm.core_service.security.filter;


import com.ssm.core_service.exception.DataExistException;
import com.ssm.core_service.exception.NotFoundException;
import com.ssm.core_service.model.constant.ApiErrorMessage;
import com.ssm.core_service.model.constant.ApiConstants;
import com.ssm.core_service.model.entity.Profile;
import com.ssm.core_service.repository.ProfileRepository;
import com.ssm.core_service.security.JwtTokenProvider;
import com.ssm.core_service.security.JwtUserPrincipal;
import com.ssm.core_service.service.ProfileService;
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
    private final ProfileRepository profileRepository;

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
            Profile profile = profileRepository.findByUserId(userId)
                    .orElseThrow(() -> new NotFoundException(
                            ApiErrorMessage.USER_PROFILE_NOT_FOUND_BY_ID.getMessage(userId)
                    ));
            List<SimpleGrantedAuthority> authorities =
                    jwtTokenProvider.getRoles(claims).stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();
            Authentication auth =
                    new UsernamePasswordAuthenticationToken(
                            new JwtUserPrincipal(profile.getId()),
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