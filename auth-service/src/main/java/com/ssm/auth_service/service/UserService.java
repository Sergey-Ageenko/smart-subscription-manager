package com.ssm.auth_service.service;

import com.ssm.auth_service.security.JwtUserPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
        JwtUserPrincipal loadUserById(String userId);
}
