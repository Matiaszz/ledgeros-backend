package com.ledgeros.lambda.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledgeros.lambda.shared.infrastructure.config.contracts.ResponseEntity;
import com.ledgeros.lambda.shared.infrastructure.config.impl.LambdaWrapper;
import com.ledgeros.lambda.shared.infrastructure.config.impl.ResponseEntityImpl;

import java.time.Instant;
import java.util.Map;

/**
 * Firebase-like Lambda Function: HealthCheck
 */
public class HealthCheckFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

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
