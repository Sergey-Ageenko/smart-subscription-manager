package com.ssm.core_service.model.entity.profileSubscription;

import com.ssm.core_service.model.entity.Profile;
import com.ssm.core_service.model.entity.Subscription;
import com.ssm.core_service.model.enums.BillingPeriod;
import com.ssm.core_service.model.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "profile_subscriptions")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSubscription {
    @EmbeddedId
    private ProfileSubscriptionId id;

    @ManyToOne
    @MapsId("profileId")
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne
    @MapsId("subscriptionId")
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscriptionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_period", nullable = false)
    private BillingPeriod billingPeriod;

    @Column(name = "next_payment_date", nullable = false)
    private LocalDate nextPaymentDate;
}
