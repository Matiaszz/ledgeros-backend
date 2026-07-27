package com.ledgeros.presentation.response;

import com.ledgeros.infrastructure.exception.ExceptionCode;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.shared.enums.ResponseStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ApiResponseTest {

    @Test
    public void testSuccessResponse() {
        ApiResponse<String> response = ApiResponse.success("Hello");
        assertEquals(ResponseStatus.SUCCESS, response.status());
        assertEquals("Hello", response.data());
        assertNull(response.code());
        assertNull(response.message());
        assertNotNull(response.timestamp());
    }

    @Test
    public void testErrorResponseFromLambdaException() {
        LambdaException exception = new LambdaException("Email already exists", ExceptionCode.EMAIL_ALREADY_EXISTS);
        ApiResponse<Object> response = ApiResponse.error(exception);

        assertEquals(ResponseStatus.ERROR, response.status());
        assertNull(response.data());
        assertEquals(ExceptionCode.EMAIL_ALREADY_EXISTS, response.code());
        assertEquals("Email already exists", response.message());
        assertNotNull(response.timestamp());
    }
}
