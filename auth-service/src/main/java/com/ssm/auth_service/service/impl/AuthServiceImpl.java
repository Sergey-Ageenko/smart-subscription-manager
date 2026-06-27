package com.ssm.auth_service.service.impl;

import com.ssm.auth_service.kafka.producer.UserProducer;
import com.ssm.auth_service.model.constants.ApiErrorMessage;
import com.ssm.auth_service.model.entities.Role;
import com.ssm.auth_service.model.entities.User;
import com.ssm.auth_service.model.enums.UserStatus;
import com.ssm.auth_service.model.exception.DataExistException;
import com.ssm.auth_service.model.exception.InvalidDataException;
import com.ssm.auth_service.model.exception.NotFoundException;
import com.ssm.auth_service.model.request.LoginRequest;
import com.ssm.auth_service.model.request.RegisterRequest;
import com.ssm.auth_service.model.response.TokenResponse;
import com.ssm.auth_service.repositories.RoleRepository;
import com.ssm.auth_service.repositories.UserRepository;
import com.ssm.auth_service.security.JwtTokenProvider;
import com.ssm.auth_service.security.JwtUserPrincipal;
import com.ssm.auth_service.service.AuthService;
import com.ssm.auth_service.service.RefreshTokenService;
import events.UserRegisteredEvent;
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
    private final UserProducer userProducer;


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

        JwtUserPrincipal jwtUserPrincipal = (JwtUserPrincipal) authentication.getPrincipal();

        if (jwtUserPrincipal.getStatus() != UserStatus.ACTIVE) {
            throw new InvalidDataException(ApiErrorMessage.USER_IS_BLOCKED.getMessage(jwtUserPrincipal.getUsername()));
        }

        String accessToken = tokenProvider.generateToken(jwtUserPrincipal);

        String refreshToken = refreshTokenService.create(jwtUserPrincipal);

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

        userProducer.send(new UserRegisteredEvent(
                savedUser.getId(),
                request.getFirstName(),
                request.getLastName()
        ));

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

    private TokenResponse getTokenResponse(User user) {

        JwtUserPrincipal jwtUserPrincipal = new JwtUserPrincipal(user);

        String accessToken = tokenProvider.generateToken(
                jwtUserPrincipal
        );

        String newRefreshToken = refreshTokenService.create(jwtUserPrincipal);

        return new TokenResponse(accessToken, newRefreshToken);
    }
}
