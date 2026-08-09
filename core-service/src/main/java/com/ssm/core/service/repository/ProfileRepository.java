package com.ssm.core.service.repository;

import com.ssm.core.service.model.entity.Profile;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Optional<Profile> findByUserId(@NotNull UUID userId);
    boolean existsByUserId(@NotNull UUID userId);
}
