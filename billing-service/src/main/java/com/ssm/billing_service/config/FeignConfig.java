package com.ssm.billing_service.config;

import com.ssm.billing_service.model.constant.ApiConstants;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return template -> template.header(
                ApiConstants.SERVICE_NAME,
                ApiConstants.BILLING_SERVICE
        );
    }
}
