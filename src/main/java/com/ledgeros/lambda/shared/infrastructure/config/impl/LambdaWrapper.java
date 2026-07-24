package com.ledgeros.lambda.shared.infrastructure.config.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledgeros.lambda.model.ApiResponse;
import com.ledgeros.lambda.shared.infrastructure.config.contracts.ResponseEntity;
import com.ledgeros.lambda.shared.infrastructure.exception.LambdaException;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

import static com.ledgeros.lambda.shared.infrastructure.exception.ExceptionCode.INTERNAL_SERVER_ERROR;

@Slf4j
public final class LambdaWrapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();
    private static final ResponseEntity RESPONSE_ENTITY = new ResponseEntityImpl();

    private LambdaWrapper() {
    }

    public static <T> APIGatewayProxyResponseEvent execute(Supplier<T> supplier) {
        try {
            T data = supplier.get();

            return buildResponse(
                    200,
                    ApiResponse.success(data)
            );

        } catch (LambdaException e) {
            log.warn(e.getLogMessage(), e);

            return buildResponse(
                    e.getStatusCode(),
                    ApiResponse.error(e)
            );

        } catch (Exception e) {
            log.error("Unexpected error during lambda execution", e);

            LambdaException exception = new LambdaException(
                    "Internal server error",
                    INTERNAL_SERVER_ERROR
            );

            return buildResponse(
                    500,
                    ApiResponse.error(exception)
            );
        }
    }

    private static <T> APIGatewayProxyResponseEvent buildResponse(
            Integer statusCode,
            T body
    ) {

        APIGatewayProxyResponseEvent response =
                RESPONSE_ENTITY.createResponse();

        response.setStatusCode(statusCode);

        try {
            response.setBody(OBJECT_MAPPER.writeValueAsString(body));
        } catch (JsonProcessingException e) {

            log.error("Could not serialize response body.", e);

            response.setStatusCode(500);

            response.setBody("""
                    {
                      "status":"ERROR",
                      "message":"Internal server error",
                      "code":"INTERNAL_SERVER_ERROR"
                    }
                    """);
        }

        return response;
    }
}