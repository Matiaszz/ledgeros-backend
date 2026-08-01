package com.ledgeros.shared.utils.parser;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.ledgeros.infrastructure.exception.LambdaException;

import static com.ledgeros.infrastructure.exception.ExceptionCode.INVALID_REQUEST;
import static com.ledgeros.shared.utils.provider.MapperProvider.OBJECT_MAPPER;

public class RequestParser {
    public <T> T parse(APIGatewayProxyRequestEvent input, Class<T> expectedParseResult){
        if (input == null || input.getBody() == null || input.getBody().isBlank()) {
            throw new LambdaException("Request body is empty", INVALID_REQUEST);
        }

        try {
            return OBJECT_MAPPER.readValue(input.getBody(), expectedParseResult);
        } catch (Exception e) {
            throw new LambdaException("Failed to parse register request body: " + e.getMessage(), INVALID_REQUEST);
        }

    }
}
