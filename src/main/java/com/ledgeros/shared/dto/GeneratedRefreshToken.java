package com.ledgeros.shared.dto;

import com.ledgeros.domain.model.RefreshToken;

public record GeneratedRefreshToken(
        String plainToken,
        RefreshTokenResponse token
) {
}
