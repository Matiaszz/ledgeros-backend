package com.ledgeros.presentation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ledgeros.shared.enums.ResponseStatus;
import com.ledgeros.infrastructure.exception.ExceptionCode;
import com.ledgeros.infrastructure.exception.LambdaException;

import java.time.Instant;

import static com.ledgeros.shared.enums.ResponseStatus.*;

/**
 * Modern Java 21 Record for API responses.
 * Jackson 2.17+ supports records automatically.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    ResponseStatus status,
    Instant timestamp,
    T data,
    ExceptionCode code,
    String message
) {
    public static <T> ApiResponse<T> success(T data) {
        Instant now = Instant.now();
        return new ApiResponse<>(SUCCESS, now, data, null, null);
    }

    public static <T> ApiResponse<T> error(LambdaException e) {
        Instant now = Instant.now();
        return new ApiResponse<>(ERROR, now, null, e.getExceptionCode(), e.getMessage());
    }

    public static <T> ApiResponse<T> error(ExceptionCode code) {
        Instant now = Instant.now();
        return new ApiResponse<>(ERROR, now, null, code, null);
    }

    public static <T> ApiResponse<T> error(String message, ExceptionCode code) {
        Instant now = Instant.now();
        return new ApiResponse<>(ERROR, now, null, code, message);
    }
}
