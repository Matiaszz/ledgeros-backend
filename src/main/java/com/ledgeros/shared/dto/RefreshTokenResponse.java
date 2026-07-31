package com.ledgeros.shared.dto;

import com.ledgeros.domain.model.RefreshToken;

import java.time.Instant;
import java.util.UUID;

public record RefreshTokenResponse(
        UUID id,
        UUID userId,
        Instant expiresAt,
        Instant createdAt,
        boolean revoked,
        Long ttl
) {
    public RefreshTokenResponse(RefreshToken token){
        this(token.getId(),
                token.getUserId(),
                token.getExpiresAt(),
                token.getCreatedAt(),
                token.isRevoked(),
                token.getTtl()
        );
    }
}
