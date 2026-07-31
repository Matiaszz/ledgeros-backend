package com.ledgeros.presentation.response;

import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String name,
        String email,
        boolean emailVerified,
        String verificationCode,
        String message
) {}
