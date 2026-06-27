package com.ssm.auth_service.service.impl;

import com.ssm.auth_service.model.constants.ApiErrorMessage;
import com.ssm.auth_service.model.entities.User;
import com.ssm.auth_service.model.exception.NotFoundException;
import com.ssm.auth_service.repositories.UserRepository;
import com.ssm.auth_service.security.JwtUserPrincipal;
import com.ssm.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    @Transactional
    public JwtUserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_USERNAME.getMessage(username)));

        return new JwtUserPrincipal(user);

    }

    @Override
    @Transactional
    public JwtUserPrincipal loadUserById(String userId) throws UsernameNotFoundException {

        User user = userRepository.findByIdWithRoles(UUID.fromString(userId))
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));

        return new JwtUserPrincipal(user);

    }


}
