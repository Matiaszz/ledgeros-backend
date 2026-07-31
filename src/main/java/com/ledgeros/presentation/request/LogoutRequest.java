package com.ledgeros.presentation.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record LogoutRequest(
        UUID refreshTokenId,
        String refreshToken
) {
    @JsonCreator
    public LogoutRequest(
            @JsonProperty("refreshTokenId") UUID refreshTokenId,
            @JsonProperty("refreshToken") String refreshToken
    ) {
        this.refreshTokenId = refreshTokenId;
        this.refreshToken = refreshToken;
    }
}
