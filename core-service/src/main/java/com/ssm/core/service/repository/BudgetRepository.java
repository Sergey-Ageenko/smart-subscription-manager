package com.ssm.core.service.repository;

import com.ssm.core.service.model.entity.Budget;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    Optional<Budget> findByProfile_UserId(@NotNull UUID userId);
}
