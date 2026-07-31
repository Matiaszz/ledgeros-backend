package com.ledgeros.presentation.response;

public record ForgotPasswordResponse(
        String email,
        String message,
        String resetCode
) {}
