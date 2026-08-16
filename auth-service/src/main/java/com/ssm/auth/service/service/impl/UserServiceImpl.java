package com.ssm.auth.service.service.impl;

import com.ssm.auth.service.model.constant.ApiErrorMessage;
import com.ssm.auth.service.model.entity.User;
import com.ssm.auth.service.repository.UserRepository;
import com.ssm.auth.service.security.UserPrincipal;
import com.ssm.auth.service.service.UserService;
import com.ssm.common.exception.NotFoundException;
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
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_USERNAME.getMessage(username)));
        return new UserPrincipal(user);
    }

    @Override
    @Transactional
    public UserPrincipal loadUserById(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByIdWithRoles(UUID.fromString(userId))
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));
        return new UserPrincipal(user);
    }


}
