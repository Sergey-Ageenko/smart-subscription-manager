package com.ssm.core_service.model.entity;

import com.ssm.core_service.model.enums.BillingPeriod;
import com.ssm.core_service.model.enums.SubscriptionCategory;
import com.ssm.core_service.model.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private SubscriptionCategory category;
}
