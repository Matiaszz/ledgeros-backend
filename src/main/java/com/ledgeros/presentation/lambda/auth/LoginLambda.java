package com.ledgeros.presentation.lambda.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ledgeros.application.auth.LoginUseCase;
import com.ledgeros.presentation.request.LoginRequest;
import com.ledgeros.shared.utils.parser.RequestParser;
import com.ledgeros.shared.utils.wrapper.LambdaWrapper;

public class LoginLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final LoginUseCase useCase = new LoginUseCase();


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        return LambdaWrapper.execute(() -> {
            LoginRequest request = RequestParser.parse(input, LoginRequest.class);

            return useCase.execute(request);
        });
    }
}
