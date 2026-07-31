package com.ledgeros.presentation.response;

import com.ledgeros.shared.enums.TokenType;
import com.ledgeros.shared.dto.GeneratedRefreshToken;

public record TokenResponse(
        GeneratedRefreshToken generatedRefreshToken,
        String accessToken,
        TokenType tokenType,
        long expiresIn
) {
    public TokenResponse(String accessToken, GeneratedRefreshToken generatedRefreshToken, long expiresIn) {
        this(generatedRefreshToken, accessToken, TokenType.BEARER, expiresIn);
    }


}
