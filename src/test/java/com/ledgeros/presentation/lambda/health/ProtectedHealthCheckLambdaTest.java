package com.ledgeros.presentation.lambda.health;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProtectedHealthCheckLambdaTest {

    @Test
    public void testProtectedHealthCheckLambda_WithAuthorizerContext() {
        ProtectedHealthCheckLambda function = new ProtectedHealthCheckLambda();
        
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        APIGatewayProxyRequestEvent.ProxyRequestContext requestContext = new APIGatewayProxyRequestEvent.ProxyRequestContext();
        
        String sampleUserId = UUID.randomUUID().toString();
        Map<String, Object> authorizerData = new HashMap<>();
        authorizerData.put("userId", sampleUserId);
        authorizerData.put("principalId", sampleUserId);
        requestContext.setAuthorizer(authorizerData);
        request.setRequestContext(requestContext);

        APIGatewayProxyResponseEvent response = function.handleRequest(request, null);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("ProtectedHealthCheckFunction"));
        assertTrue(response.getBody().contains("UP"));
        assertTrue(response.getBody().contains(sampleUserId));
    }

    @Test
    public void testProtectedHealthCheckLambda_WithoutAuthorizerContext() {
        ProtectedHealthCheckLambda function = new ProtectedHealthCheckLambda();
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

        APIGatewayProxyResponseEvent response = function.handleRequest(request, null);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("ProtectedHealthCheckFunction"));
        assertTrue(response.getBody().contains("UP"));
    }
}
