package com.ledgeros.presentation.response;

import com.ledgeros.domain.model.User;
import com.ledgeros.presentation.request.RegisterRequest;

import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String name,
        String email,
        boolean emailVerified,
        String verificationCode,
        String message
) {
    public RegisterResponse(User u, String message) {
        this(u.getId(), u.getName(), u.getEmail(), u.isEmailVerified(), u.getVerificationCode(), message);
    }
}
