package com.ledgeros.lambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ledgeros.lambda.presentation.lambda.health.HealthCheckLambda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HandlerTest {

    @Test
    public void testHealthCheckFunction() {
        HealthCheckLambda function = new HealthCheckLambda();
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setPath("/healthcheck");

        APIGatewayProxyResponseEvent response = function.handleRequest(request, null);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("HealthCheckFunction"));
        assertTrue(response.getBody().contains("UP"));
    }

}
