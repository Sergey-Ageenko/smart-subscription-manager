package com.ssm.auth_service.model.response;

import com.ssm.auth_service.model.constants.ApiMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse implements Serializable {
    private String message;
    private String accessToken;
    private boolean success;

    public static  AuthResponse  createSuccessfulWithNewUser(String token){
        return new AuthResponse(ApiMessage.USER_CREATED.getMessage(), token, true);
    }

    public static AuthResponse createSuccessfulWithNewToken(String token){
        return new AuthResponse (ApiMessage.TOKEN_CREATED_OR_UPDATED.getMessage(), token, true);
    }
}
