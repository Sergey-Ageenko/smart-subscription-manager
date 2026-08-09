package com.ssm.auth.service.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters long")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Password must be 8-20 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    @NotBlank(message = "Password cannot be empty")
    private String password;

    @Size(min = 2, max = 30, message = "The firstname length must be no more than 255 characters.")
    @NotBlank(message = "Firstname cannot be empty")
    private String firstName;

    @Size(min = 2, max = 80, message = "The lastname length must be no more than 255 characters.")
    @NotBlank(message = "Lastname cannot be empty")
    private String lastName;
}
