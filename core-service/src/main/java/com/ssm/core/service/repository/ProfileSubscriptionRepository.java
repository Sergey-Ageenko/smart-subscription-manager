package com.ssm.core.service.repository;

import com.ssm.core.service.model.entity.ProfileSubscription;
import com.ssm.core.service.model.enums.SubscriptionStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileSubscriptionRepository extends JpaRepository<ProfileSubscription, UUID> {
    Optional<ProfileSubscription> findByProfile_UserIdAndSubscription_Id(@NotNull UUID userId, @NotNull UUID subscriptionId);
    boolean existsByProfile_UserIdAndSubscription_Id(@NotNull UUID userId, @NotNull UUID subscriptionId);
    List<ProfileSubscription> findAllByProfile_UserIdAndStatus(@NotNull UUID userId, SubscriptionStatus active);

    @EntityGraph(attributePaths = "subscription")
    List<ProfileSubscription> findAllByProfile_UserId(@NotNull UUID userId);
}
