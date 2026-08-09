package com.ssm.core.service.model.request.user;

import jakarta.validation.constraints.Size;

public record ProfileUpdateRequest(

        @Size(min = 2, max = 30)
        String firstName,

        @Size(min = 2, max = 80)
        String lastName

) {
}
