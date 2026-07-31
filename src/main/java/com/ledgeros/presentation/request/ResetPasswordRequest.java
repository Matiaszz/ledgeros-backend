package com.ledgeros.presentation.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ResetPasswordRequest(
        String email,
        String resetCode,
        String newPassword
) {
    @JsonCreator
    public ResetPasswordRequest(
            @JsonProperty("email") String email,
            @JsonProperty("resetCode") String resetCode,
            @JsonProperty("newPassword") String newPassword
    ) {
        this.email = email;
        this.resetCode = resetCode;
        this.newPassword = newPassword;
    }
}
