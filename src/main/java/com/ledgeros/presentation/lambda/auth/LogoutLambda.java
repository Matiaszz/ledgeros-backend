package com.ledgeros.presentation.lambda.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledgeros.application.auth.LogoutUseCase;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.presentation.request.LogoutRequest;
import com.ledgeros.shared.utils.wrapper.LambdaWrapper;

import static com.ledgeros.infrastructure.exception.ExceptionCode.INVALID_REQUEST;

public class LogoutLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final LogoutUseCase useCase = new LogoutUseCase();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        return LambdaWrapper.execute(() -> {
            LogoutRequest request = null;
            if (input != null && input.getBody() != null && !input.getBody().isBlank()) {
                try {
                    request = OBJECT_MAPPER.readValue(input.getBody(), LogoutRequest.class);
                } catch (Exception e) {
                    throw new LambdaException("Failed to parse logout request body: " + e.getMessage(), INVALID_REQUEST);
                }
            }

            return useCase.execute(request);
        });
    }
}
