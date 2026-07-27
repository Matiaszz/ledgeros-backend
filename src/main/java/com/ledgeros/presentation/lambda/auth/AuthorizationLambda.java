package com.ledgeros.presentation.lambda.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayCustomAuthorizerEvent;
import com.amazonaws.services.lambda.runtime.events.IamPolicyResponse;
import com.ledgeros.application.AuthorizationUseCase;
import com.ledgeros.shared.utils.LambdaWrapper;

public class AuthorizationLambda implements RequestHandler<APIGatewayCustomAuthorizerEvent, IamPolicyResponse> {

    private final AuthorizationUseCase useCase = new AuthorizationUseCase();

    @Override
    public IamPolicyResponse handleRequest(APIGatewayCustomAuthorizerEvent input, Context context) {
        return useCase.execute(input);
    }
}
