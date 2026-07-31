package com.ledgeros.presentation.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record VerifyEmailRequest(
        String email,
        String verificationCode
) {
    @JsonCreator
    public VerifyEmailRequest(
            @JsonProperty("email") String email,
            @JsonProperty("verificationCode") String verificationCode
    ) {
        this.email = email;
        this.verificationCode = verificationCode;
    }
}
