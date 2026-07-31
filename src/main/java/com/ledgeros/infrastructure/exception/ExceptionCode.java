package com.ledgeros.infrastructure.exception;

public enum ExceptionCode {
    EMAIL_ALREADY_EXISTS,
    UNAUTHORIZED,
    FORBIDDEN,
    INVALID_REFRESH_TOKEN,
    INVALID_REFRESH_TOKEN_ID,
    REFRESH_TOKEN_EXPIRED,
    INTERNAL_SERVER_ERROR;

    public int getStatusCode() {
        return switch (this) {
            case EMAIL_ALREADY_EXISTS -> 409;
            case INVALID_REFRESH_TOKEN_ID -> 400;
            case UNAUTHORIZED, INVALID_REFRESH_TOKEN, REFRESH_TOKEN_EXPIRED -> 401;
            case FORBIDDEN -> 403;
            default -> 500;
        };
    }
}
