package com.ledgeros.presentation.lambda.auth;

import com.amazonaws.services.lambda.runtime.events.APIGatewayCustomAuthorizerEvent;
import com.amazonaws.services.lambda.runtime.events.IamPolicyResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizationLambdaTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testHandleRequest_ReturnsPolicyResponse() {
        AuthorizationLambda lambda = new AuthorizationLambda();
        APIGatewayCustomAuthorizerEvent event = new APIGatewayCustomAuthorizerEvent();
        event.setAuthorizationToken("invalid.token");
        event.setMethodArn("arn:aws:execute-api:us-east-1:123456789012:api-id/stage/GET/resource");

        IamPolicyResponse response = lambda.handleRequest(event, null);

        assertNotNull(response);
        assertEquals("user", response.getPrincipalId());
        Map<String, Object>[] statements = (Map<String, Object>[]) response.getPolicyDocument().get("Statement");
        assertEquals("Deny", statements[0].get("Effect"));
    }
}
