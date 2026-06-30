package com.ssm.core_service.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.core_service.utils.ApiError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ApiError apiError = new ApiError(
                403,
                "FORBIDDEN",
                accessDeniedException.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );
        response.getWriter().write(
                objectMapper.writeValueAsString(apiError)
        );
    }
}

