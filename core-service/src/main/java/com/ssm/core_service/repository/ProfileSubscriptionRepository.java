package com.ssm.core_service.repository;

import com.ssm.core_service.model.entity.profileSubscription.ProfileSubscription;
import com.ssm.core_service.model.entity.profileSubscription.ProfileSubscriptionId;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileSubscriptionRepository extends JpaRepository<ProfileSubscription, ProfileSubscriptionId> {
    Optional<ProfileSubscription> findById_ProfileIdAndSubscriptionId(UUID idProfileId, UUID idSubscriptionId);
    boolean existsById_SubscriptionId(UUID idSubscriptionId);
    List<ProfileSubscription>findAllByProfile_Id(UUID profileId);
}
