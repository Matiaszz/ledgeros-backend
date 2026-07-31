package com.ledgeros.presentation.request;

import java.util.UUID;

public record RefreshTokenRequest(
        UUID refreshTokenId,
        String refreshToken
) {}
