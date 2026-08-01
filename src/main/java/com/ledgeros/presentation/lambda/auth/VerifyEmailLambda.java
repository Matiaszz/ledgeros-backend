package com.ledgeros.presentation.lambda.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ledgeros.application.auth.VerifyEmailUseCase;
import com.ledgeros.presentation.request.VerifyEmailRequest;
import com.ledgeros.shared.utils.parser.RequestParser;
import com.ledgeros.shared.utils.wrapper.LambdaWrapper;


public class VerifyEmailLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final VerifyEmailUseCase useCase = new VerifyEmailUseCase();


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        return LambdaWrapper.execute(() -> {
            VerifyEmailRequest request = RequestParser.parse(input, VerifyEmailRequest.class);

            return useCase.execute(request);
        });
    }
}
