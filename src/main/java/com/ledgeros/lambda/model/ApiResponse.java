package com.ledgeros.lambda.model;

import com.ledgeros.lambda.shared.infrastructure.config.ResponseStatus;
import com.ledgeros.lambda.shared.infrastructure.exception.ExceptionCode;
import com.ledgeros.lambda.shared.infrastructure.exception.LambdaException;

import java.time.Instant;

import static com.ledgeros.lambda.shared.infrastructure.config.ResponseStatus.*;

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
