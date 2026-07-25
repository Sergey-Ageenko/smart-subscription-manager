package com.ssm.billing_service.model.entity;

import com.ssm.billing_service.model.enums.BudgetStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "expense_forecasts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    private UUID userId;

    @Column(name = "monthly_expenses", precision = 10, scale = 2)
    private BigDecimal monthlyExpenses;

    @Column(name = "remaining_budget", precision = 10, scale = 2)
    private BigDecimal remainingBudget;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BudgetStatus status;

    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;
}
