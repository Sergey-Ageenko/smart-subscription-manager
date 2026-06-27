package com.ssm.auth_service.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters long")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Size(min = 8, max = 255, message = "The password length must be no more than 255 characters.")
    @NotBlank(message = "Password cannot be empty")
    private String password;

    @Size(min = 2, max = 255, message = "The firstname length must be no more than 255 characters.")
    @NotBlank(message = "Firstname cannot be empty")
    private String firstName;

    @Size(min = 2, max = 255, message = "The lastname length must be no more than 255 characters.")
    @NotBlank(message = "Lastname cannot be empty")
    private String lastName;
}
