package com.ssm.auth_service.service.impl;

import com.ssm.auth_service.service.TokenBlacklistService;
import com.ssm.common.exception.DataExistException;
import com.ssm.common.exception.InvalidDataException;
import com.ssm.common.exception.NotFoundException;
import com.ssm.auth_service.factory.UserEventFactory;
import com.ssm.auth_service.model.constant.ApiErrorMessage;
import com.ssm.auth_service.model.entity.OutboxEvent;
import com.ssm.auth_service.model.entity.Role;
import com.ssm.auth_service.model.entity.User;
import com.ssm.auth_service.model.enums.UserStatus;
import com.ssm.auth_service.model.request.LoginRequest;
import com.ssm.auth_service.model.request.RegisterRequest;
import com.ssm.auth_service.model.response.TokenResponse;
import com.ssm.auth_service.repository.OutboxRepository;
import com.ssm.auth_service.repository.RoleRepository;
import com.ssm.auth_service.repository.UserRepository;
import com.ssm.auth_service.security.JwtTokenProvider;
import com.ssm.auth_service.security.UserPrincipal;
import com.ssm.auth_service.service.AuthService;
import com.ssm.auth_service.service.RefreshTokenService;
import com.ssm.common.exception.UserBlockedException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static com.ssm.auth_service.model.enums.RoleType.ROLE_USER;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager manager;
    private final OutboxRepository outboxRepository;
    private final UserEventFactory eventFactory;
    private final TokenBlacklistService tokenBlacklistService;


    @Override
    @Transactional
    public TokenResponse login(@NotNull LoginRequest request) {
        Authentication authentication;
        try {
            authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new InvalidDataException(ApiErrorMessage.INVALID_USER_OR_PASSWORD.getMessage());
        }
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if (userPrincipal.getStatus() != UserStatus.ACTIVE) {
            throw new UserBlockedException(ApiErrorMessage.USER_IS_BLOCKED.getMessage(userPrincipal.getUsername()));
        }
        String accessToken = tokenProvider.generateToken(userPrincipal);
        String refreshToken = refreshTokenService.create(userPrincipal);
        return new TokenResponse(accessToken, refreshToken);
    }


    @Override
    @Transactional
    public TokenResponse register(@NotNull RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DataExistException(ApiErrorMessage.USER_WITH_USERNAME_ALREADY_EXISTS.getMessage(request.getUsername()));
        }
        Role userRole = roleRepository.findByType(ROLE_USER)
                .orElseThrow(() ->
                        new NotFoundException(ApiErrorMessage.ROLE_NOT_FOUND.getMessage(ROLE_USER)));
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .roles(Set.of(userRole))
                .build();
        User savedUser = userRepository.save(user);
        OutboxEvent outboxEvent = eventFactory.registered(user.getId(), request);
        outboxRepository.save(outboxEvent);
        return getTokenResponse(savedUser);
    }

    @Override
    @Transactional
    public TokenResponse refresh(@NotNull String refreshToken) {
        UUID userId = refreshTokenService.validate(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));
        refreshTokenService.delete(refreshToken);
        return getTokenResponse(user);
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        tokenBlacklistService.blacklist(accessToken);
        refreshTokenService.delete(refreshToken);
    }

    private TokenResponse getTokenResponse(User user) {
        UserPrincipal userPrincipal = new UserPrincipal(user);
        String accessToken = tokenProvider.generateToken(
                userPrincipal
        );
        String newRefreshToken = refreshTokenService.create(userPrincipal);
        return new TokenResponse(accessToken, newRefreshToken);
    }
}
