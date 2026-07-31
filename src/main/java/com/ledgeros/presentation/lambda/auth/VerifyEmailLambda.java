package com.ledgeros.presentation.lambda.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledgeros.application.auth.VerifyEmailUseCase;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.presentation.request.VerifyEmailRequest;
import com.ledgeros.shared.utils.wrapper.LambdaWrapper;

import static com.ledgeros.infrastructure.exception.ExceptionCode.INVALID_REQUEST;

public class VerifyEmailLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final VerifyEmailUseCase useCase = new VerifyEmailUseCase();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        return LambdaWrapper.execute(() -> {
            if (input == null || input.getBody() == null || input.getBody().isBlank()) {
                throw new LambdaException("Request body is empty", INVALID_REQUEST);
            }

            VerifyEmailRequest request;
            try {
                request = OBJECT_MAPPER.readValue(input.getBody(), VerifyEmailRequest.class);
            } catch (Exception e) {
                throw new LambdaException("Failed to parse verify email request body: " + e.getMessage(), INVALID_REQUEST);
            }

            return useCase.execute(request);
        });
    }
}
