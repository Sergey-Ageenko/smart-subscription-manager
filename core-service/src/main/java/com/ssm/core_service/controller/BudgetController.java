package com.ssm.core_service.controller;

import com.ssm.core_service.model.request.BudgetUpdateRequest;
import com.ssm.core_service.model.response.BudgetResponse;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.security.JwtUserPrincipal;
import com.ssm.core_service.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<CoreResponse<BudgetResponse>> getBudget(@AuthenticationPrincipal JwtUserPrincipal principal){
        return ResponseEntity.ok()
                        .body(budgetService.getBudget(principal.userId()));
    }

    @PutMapping("/update")
    public ResponseEntity<CoreResponse<BudgetResponse>> update(@AuthenticationPrincipal JwtUserPrincipal principal, @Valid @RequestBody BudgetUpdateRequest request){
        return ResponseEntity.ok()
                .body(budgetService.updateBudget(principal.userId(), request));
    }
}
