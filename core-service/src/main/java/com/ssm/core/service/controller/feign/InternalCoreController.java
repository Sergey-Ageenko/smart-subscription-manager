package com.ssm.core.service.controller.feign;

import com.ssm.core.service.model.response.feign.BillingBudgetSettingsResponse;
import com.ssm.core.service.model.response.feign.BillingProfileSubscriptionResponse;
import com.ssm.core.service.security.UserPrincipal;
import com.ssm.core.service.service.FeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalCoreController {

    private final FeignService feignService;

    @GetMapping("/budget")
    public BillingBudgetSettingsResponse getBudget(@AuthenticationPrincipal UserPrincipal principal){
        return feignService.getBudgetSettings(principal.userId());
    }

    @GetMapping("/subscriptions")
    public List<BillingProfileSubscriptionResponse> getAllSubscriptions(@AuthenticationPrincipal UserPrincipal principal){
        return feignService.getAllActiveSubscriptions(principal.userId());
    }

}
