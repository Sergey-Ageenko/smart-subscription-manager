package com.ssm.core_service.model.request;

import jakarta.validation.constraints.Size;

public record ProfileUpdateRequest(

        @Size(min = 2, max = 255)
        String firstName,

        @Size(min = 2, max = 255)
        String lastName

) {
}
