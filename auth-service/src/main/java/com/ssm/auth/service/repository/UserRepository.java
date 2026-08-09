package com.ssm.auth.service.repository;

import com.ssm.auth.service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    @Query("""
                select u from User u
                join fetch u.roles
                where u.username = :username
            """)
    Optional<User> findByUsernameWithRoles(String username);

    @Query("""
                select u from User u
                join fetch u.roles
                where u.id = :userId
            """)
    Optional<User> findByIdWithRoles(UUID userId);

}
