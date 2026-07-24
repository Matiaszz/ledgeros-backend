package com.ledgeros.lambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ledgeros.lambda.functions.CreateTransactionFunction;
import com.ledgeros.lambda.functions.HealthCheckFunction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HandlerTest {

    @Test
    public void testHealthCheckFunction() {
        HealthCheckFunction function = new HealthCheckFunction();
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setPath("/healthcheck");

        APIGatewayProxyResponseEvent response = function.handleRequest(request, null);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("HealthCheckFunction"));
        assertTrue(response.getBody().contains("UP"));
    }

    @Test
    public void testCreateTransactionFunction() {
        CreateTransactionFunction function = new CreateTransactionFunction();
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setPath("/transactions");
        request.setHttpMethod("POST");

        APIGatewayProxyResponseEvent response = function.handleRequest(request, null);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("tx-9999"));
        assertTrue(response.getBody().contains("SUCCESS"));
    }
}
