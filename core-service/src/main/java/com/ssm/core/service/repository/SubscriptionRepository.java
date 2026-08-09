package com.ssm.core.service.repository;

import com.ssm.core.service.model.entity.Subscription;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    boolean existsByName(@NotNull String name);
}
