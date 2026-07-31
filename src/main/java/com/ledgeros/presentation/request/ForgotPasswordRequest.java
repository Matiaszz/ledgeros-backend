package com.ledgeros.presentation.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ForgotPasswordRequest(
        String email
) {
    @JsonCreator
    public ForgotPasswordRequest(
            @JsonProperty("email") String email
    ) {
        this.email = email;
    }
}
