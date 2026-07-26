package com.ledgeros;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ledgeros.presentation.lambda.health.HealthCheckLambda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HandlerTest {

    private final APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

    @Test
    public void testHealthCheckLambda() {
        HealthCheckLambda function = new HealthCheckLambda();
        request.setPath("/healthcheck");

        APIGatewayProxyResponseEvent response = function.handleRequest(request, null);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("HealthCheckFunction"));
        assertTrue(response.getBody().contains("UP"));
    }

}
