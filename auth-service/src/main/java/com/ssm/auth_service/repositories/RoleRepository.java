package com.ssm.auth_service.repositories;

import com.ssm.auth_service.model.entities.Role;
import com.ssm.auth_service.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByType(RoleType type);
}
