package com.ssm.core_service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ApiLoggingAspect {

    @Around("@annotation(com.ssm.auth_service.aspect.annotation.ApiLog) || @within(com.ssm.auth_service.aspect.annotation.ApiLog)")
    public Object logApiCall(ProceedingJoinPoint jp) throws Throwable {

        String method = jp.getSignature().toString();
        long start = System.currentTimeMillis();

        log.info("api_start method = {}", method);

        try {
            Object result = jp.proceed();
            long duration = System.currentTimeMillis() - start;

            log.info("api_end method = {} duration_ms = {}", method, duration);

            return result;
        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - start;

            log.warn("api_fail method = {} duration_ms = {} error = {}",
                    method, duration, ex.getMessage());

            throw ex;
        }
    }
}
