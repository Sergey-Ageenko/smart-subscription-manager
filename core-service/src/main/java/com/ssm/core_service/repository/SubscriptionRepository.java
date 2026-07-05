package com.ssm.core_service.repository;

import com.ssm.core_service.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findAllByProfile_Id(UUID profileId);
    Optional<Subscription> findByIdAndProfile_Id(UUID subId, UUID profileId);
    boolean existsByName(String name);
}
