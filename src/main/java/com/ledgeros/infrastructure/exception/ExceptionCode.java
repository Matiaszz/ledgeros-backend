package com.ledgeros.infrastructure.exception;

public enum ExceptionCode {
    EMAIL_ALREADY_EXISTS,
    UNAUTHORIZED,
    FORBIDDEN,
    INTERNAL_SERVER_ERROR;

    public int getStatusCode() {
        switch (this) {
            case EMAIL_ALREADY_EXISTS:
                return 409;
            case UNAUTHORIZED:
                return 401;
            case FORBIDDEN:
                return 403;
            default:
                return 500;
        }
    }
}
