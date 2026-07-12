package com.ssm.core_service.model.entity.profileSubscription;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProfileSubscriptionId implements Serializable {
    @Column(name = "profile_id")
    private UUID profileId;

    @Column(name = "subscription_id")
    private UUID subscriptionId;
}
