package com.ledgeros.presentation.lambda.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ledgeros.application.auth.LogoutUseCase;
import com.ledgeros.presentation.request.LogoutRequest;
import com.ledgeros.shared.utils.parser.RequestParser;
import com.ledgeros.shared.utils.wrapper.LambdaWrapper;


public class LogoutLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final LogoutUseCase useCase = new LogoutUseCase();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        return LambdaWrapper.execute(() -> {
            LogoutRequest request = RequestParser.parse(input, LogoutRequest.class);

            return useCase.execute(request);
        });
    }
}
