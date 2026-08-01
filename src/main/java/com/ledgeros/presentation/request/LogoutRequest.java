package com.ledgeros.presentation.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record LogoutRequest(
        UUID refreshTokenId
) {
    @JsonCreator
    public LogoutRequest(
            @JsonProperty("refreshTokenId") UUID refreshTokenId
    ) {
        this.refreshTokenId = refreshTokenId;
    }
}
