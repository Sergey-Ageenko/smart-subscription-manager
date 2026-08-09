package com.ssm.auth.service.service;

import com.ssm.auth.service.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
        UserPrincipal loadUserById(String userId);
}
