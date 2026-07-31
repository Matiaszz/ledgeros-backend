package com.ledgeros.presentation.lambda.health;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ledgeros.shared.utils.wrapper.LambdaWrapper;

import java.time.Instant;
import java.util.Map;

/**
 * Firebase-like Lambda Function: HealthCheck
 */
public class HealthCheckLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        return LambdaWrapper.execute(() ->
            Map.of(
                    "status", "UP",
                    "function", "HealthCheckFunction",
                    "timestamp", Instant.now()
            )
        );
    }

}
