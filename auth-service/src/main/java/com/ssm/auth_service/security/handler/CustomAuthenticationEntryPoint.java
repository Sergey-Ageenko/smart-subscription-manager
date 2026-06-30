package com.ssm.auth_service.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.auth_service.model.constants.ApiConstants;
import com.ssm.auth_service.utils.ApiError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Object attr = request.getAttribute(ApiConstants.JWT_ERROR);
        String message = (attr != null)
                ? attr.toString()
                : authException.getMessage();
        ApiError apiError = new ApiError(
                401,
                "UNAUTHORIZED",
                message,
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );
        response.getWriter().write(
                objectMapper.writeValueAsString(apiError)
        );
    }
}
