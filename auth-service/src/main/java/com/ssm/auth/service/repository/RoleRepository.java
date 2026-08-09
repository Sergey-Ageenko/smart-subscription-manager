package com.ssm.auth.service.repository;

import com.ssm.auth.service.model.entity.Role;
import com.ssm.auth.service.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByType(RoleType type);
}
