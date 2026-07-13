package com.ssm.core_service.repository;

import com.ssm.core_service.model.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    Optional<Budget> findByProfile_Id(UUID profileId);
}
