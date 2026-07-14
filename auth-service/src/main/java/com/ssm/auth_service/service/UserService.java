package com.ssm.auth_service.service;

import com.ssm.auth_service.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
        UserPrincipal loadUserById(String userId);
}
