package com.ledgeros.presentation.lambda.health;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ledgeros.shared.utils.LambdaWrapper;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * HealthCheck Lambda for Protected Routes to verify Authorizer context output
 */
public class ProtectedHealthCheckLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        return LambdaWrapper.execute(() -> {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", "UP");
            responseData.put("function", "ProtectedHealthCheckFunction");
            responseData.put("timestamp", Instant.now().toString());

            if (input != null && input.getRequestContext() != null && input.getRequestContext().getAuthorizer() != null) {
                Map<String, Object> authorizerContext = input.getRequestContext().getAuthorizer();
                responseData.put("authorizerContext", authorizerContext);
                responseData.put("userId", authorizerContext.get("userId"));
                responseData.put("principalId", authorizerContext.get("principalId"));
            } else {
                responseData.put("authorizerContext", null);
            }

            return responseData;
        });
    }
}
