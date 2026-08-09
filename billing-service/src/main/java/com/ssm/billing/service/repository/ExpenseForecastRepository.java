package com.ssm.billing.service.repository;

import com.ssm.billing.service.model.entity.ExpenseForecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExpenseForecastRepository extends JpaRepository<ExpenseForecast, UUID> {
    Optional<ExpenseForecast> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}
