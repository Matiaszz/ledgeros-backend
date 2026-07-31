package com.ledgeros.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ledgeros.shared.enums.TokenType;
import com.ledgeros.shared.dto.GeneratedRefreshToken;

public record TokenResponse(
        @JsonProperty("generatedRefreshToken") GeneratedRefreshToken generatedRefreshToken,
        @JsonProperty("accessToken") String accessToken,
        @JsonProperty("tokenType") TokenType tokenType,
        @JsonProperty("expiresIn") long expiresIn,
        @JsonProperty("name") String name
) {
    public TokenResponse(String accessToken, GeneratedRefreshToken generatedRefreshToken, long expiresIn, String name) {
        this(generatedRefreshToken, accessToken, TokenType.BEARER, expiresIn, name);
    }

    public TokenResponse(String accessToken, GeneratedRefreshToken generatedRefreshToken, long expiresIn) {
        this(generatedRefreshToken, accessToken, TokenType.BEARER, expiresIn, null);
    }
}
