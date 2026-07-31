package com.ledgeros.presentation.lambda.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledgeros.application.auth.LoginUseCase;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.presentation.request.LoginRequest;
import com.ledgeros.shared.utils.wrapper.LambdaWrapper;

import static com.ledgeros.infrastructure.exception.ExceptionCode.INVALID_REQUEST;

public class LoginLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final LoginUseCase useCase = new LoginUseCase();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        return LambdaWrapper.execute(() -> {
            if (input == null || input.getBody() == null || input.getBody().isBlank()) {
                throw new LambdaException("Request body is empty", INVALID_REQUEST);
            }

            LoginRequest request;
            try {
                request = OBJECT_MAPPER.readValue(input.getBody(), LoginRequest.class);
            } catch (Exception e) {
                throw new LambdaException("Failed to parse login request body: " + e.getMessage(), INVALID_REQUEST);
            }

            return useCase.execute(request);
        });
    }
}
