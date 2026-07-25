package com.ledgeros.lambda.presentation.response;

import com.ledgeros.lambda.shared.enums.ResponseStatus;
import com.ledgeros.lambda.infrastructure.exception.ExceptionCode;
import com.ledgeros.lambda.infrastructure.exception.LambdaException;

import java.time.Instant;

import static com.ledgeros.lambda.shared.enums.ResponseStatus.*;

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
