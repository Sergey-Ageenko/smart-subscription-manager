package com.ssm.billing_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingResponse<P> implements Serializable {
    private String message;
    private P payload;
    private boolean success;

    public static <P> BillingResponse<P> createSuccessful(P payload) {
        return new BillingResponse<>(" ", payload, true);

    }
}
