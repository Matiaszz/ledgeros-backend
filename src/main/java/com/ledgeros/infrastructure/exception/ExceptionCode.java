package com.ledgeros.infrastructure.exception;

public enum ExceptionCode {
    EMAIL_ALREADY_EXISTS,
    UNAUTHORIZED,
    FORBIDDEN,
    INVALID_REFRESH_TOKEN,
    REFRESH_TOKEN_EXPIRED,
    INTERNAL_SERVER_ERROR;

    public int getStatusCode() {
        switch (this) {
            case EMAIL_ALREADY_EXISTS:
                return 409;
            case UNAUTHORIZED:
            case INVALID_REFRESH_TOKEN:
            case REFRESH_TOKEN_EXPIRED:
                return 401;
            case FORBIDDEN:
                return 403;
            default:
                return 500;
        }
    }
}
