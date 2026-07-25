package com.ssm.core_service.repository;

import com.ssm.core_service.model.entity.Subscription;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    boolean existsByName(@NotNull String name);
}
