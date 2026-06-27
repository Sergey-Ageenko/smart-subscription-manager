package com.ssm.core_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoreResponse<P> implements Serializable {
    private String message;
    private P payload;
    private boolean success;


    public static <P> CoreResponse<P> createSuccessful(P payload) {
        return new CoreResponse<>(" ", payload, true);

    }
}
