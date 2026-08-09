package com.ssm.billing.service.controller;

import com.ssm.billing.service.model.response.BillingResponse;
import com.ssm.billing.service.model.response.BudgetStatusResponse;
import com.ssm.billing.service.security.UserPrincipal;
import com.ssm.billing.service.service.ForecastService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/billing/budgets")
public class BudgetStatusController {

    private final ForecastService forecastService;

    @GetMapping("/status")
    public ResponseEntity<BillingResponse<BudgetStatusResponse>> getBudgetStatus(@AuthenticationPrincipal UserPrincipal principal){
        return ResponseEntity.ok()
                .body(forecastService.getBudgetStatus(principal.userId()));
    }
}
