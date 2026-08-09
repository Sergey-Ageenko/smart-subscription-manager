package com.ssm.core.service.controller;

import com.ssm.core.service.model.request.user.BudgetUpdateRequest;
import com.ssm.core.service.model.response.BudgetResponse;
import com.ssm.core.service.model.response.CoreResponse;
import com.ssm.core.service.security.UserPrincipal;
import com.ssm.core.service.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/core/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<CoreResponse<BudgetResponse>> getBudget(@AuthenticationPrincipal UserPrincipal principal){
        return ResponseEntity.ok()
                        .body(budgetService.getBudget(principal.userId()));
    }

    @PutMapping
    public ResponseEntity<CoreResponse<BudgetResponse>> update(@AuthenticationPrincipal UserPrincipal principal, @Valid @RequestBody BudgetUpdateRequest request) {
        return ResponseEntity.ok()
                .body(budgetService.updateBudget(principal.userId(), request));
    }
}
