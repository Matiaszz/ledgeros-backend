package com.ledgeros.presentation.lambda.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ledgeros.application.auth.ForgotPasswordUseCase;
import com.ledgeros.presentation.request.ForgotPasswordRequest;
import com.ledgeros.shared.utils.parser.RequestParser;
import com.ledgeros.shared.utils.wrapper.LambdaWrapper;

public class ForgotPasswordLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ForgotPasswordUseCase useCase = new ForgotPasswordUseCase();
    private final RequestParser parser = new RequestParser();


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        return LambdaWrapper.execute(() -> {
            ForgotPasswordRequest request = parser.parse(input, ForgotPasswordRequest.class);

            return useCase.execute(request);
        });
    }
}
