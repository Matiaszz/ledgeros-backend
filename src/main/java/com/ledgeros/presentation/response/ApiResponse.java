package com.ledgeros.presentation.response;

import com.ledgeros.shared.enums.ResponseStatus;
import com.ledgeros.infrastructure.exception.ExceptionCode;
import com.ledgeros.infrastructure.exception.LambdaException;

import java.time.Instant;

import static com.ledgeros.shared.enums.ResponseStatus.*;

/**
 * Modern Java 21 Record for API responses.
 * Jackson 2.17+ supports records automatically.
 */
public record ApiResponse<T>(
    ResponseStatus status,
    Instant timestamp,
    T data,
    ExceptionCode code
) {
    public static <T> ApiResponse<T> success(T data) {
        Instant now = Instant.now();
        return new ApiResponse<>(SUCCESS, now, data, null);
    }

    public static <T> ApiResponse<T> error(LambdaException e) {
        Instant now = Instant.now();
        return new ApiResponse<>(ERROR, now, null, e.getExceptionCode());
    }


}
