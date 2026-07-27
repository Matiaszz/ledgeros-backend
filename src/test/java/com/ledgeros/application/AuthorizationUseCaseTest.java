package com.ledgeros.application;

import com.amazonaws.services.lambda.runtime.events.APIGatewayCustomAuthorizerEvent;
import com.amazonaws.services.lambda.runtime.events.IamPolicyResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ledgeros.domain.model.User;
import com.ledgeros.mocks.repositories.MockUserRepository;
import com.ledgeros.shared.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizationUseCaseTest {

    private static final String SECRET = System.getenv().getOrDefault("JWT_SECRET", "secreto_dev_fallback");

    private MockUserRepository repository;
    private AuthorizationUseCase authorizationUseCase;

    @BeforeEach
    public void setUp() {
        repository = new MockUserRepository();
        UserUtils userUtils = new UserUtils(repository);
        authorizationUseCase = new AuthorizationUseCase(userUtils);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testExecute_InvalidToken_ReturnsDenyPolicy() {
        APIGatewayCustomAuthorizerEvent event = new APIGatewayCustomAuthorizerEvent();
        event.setAuthorizationToken("invalid.token.here");
        event.setMethodArn("arn:aws:execute-api:us-east-1:123456789012:api-id/stage/GET/resource");

        IamPolicyResponse response = authorizationUseCase.execute(event);

        assertNotNull(response);
        assertEquals("user", response.getPrincipalId());
        assertNotNull(response.getPolicyDocument());
        Map<String, Object>[] statements = (Map<String, Object>[]) response.getPolicyDocument().get("Statement");
        assertEquals("Deny", statements[0].get("Effect"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testExecute_NullToken_ReturnsDenyPolicy() {
        APIGatewayCustomAuthorizerEvent event = new APIGatewayCustomAuthorizerEvent();
        event.setMethodArn("arn:aws:execute-api:us-east-1:123456789012:api-id/stage/GET/resource");

        IamPolicyResponse response = authorizationUseCase.execute(event);

        assertNotNull(response);
        assertEquals("user", response.getPrincipalId());
        Map<String, Object>[] statements = (Map<String, Object>[]) response.getPolicyDocument().get("Statement");
        assertEquals("Deny", statements[0].get("Effect"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testExecute_ValidTokenUserNotFound_ReturnsDenyPolicy() {
        UUID userId = UUID.randomUUID();
        String token = JWT.create()
                .withIssuer("ledgeros-backend")
                .withSubject(userId.toString())
                .sign(Algorithm.HMAC256(SECRET));

        APIGatewayCustomAuthorizerEvent event = new APIGatewayCustomAuthorizerEvent();
        event.setAuthorizationToken("Bearer " + token);
        event.setMethodArn("arn:aws:execute-api:us-east-1:123456789012:api-id/stage/GET/resource");

        IamPolicyResponse response = authorizationUseCase.execute(event);

        assertNotNull(response);
        assertEquals("user", response.getPrincipalId());
        Map<String, Object>[] statements = (Map<String, Object>[]) response.getPolicyDocument().get("Statement");
        assertEquals("Deny", statements[0].get("Effect"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testExecute_ValidTokenAndUser_ReturnsAllowPolicy() {
        UUID userId = UUID.randomUUID();
        String token = JWT.create()
                .withIssuer("ledgeros-backend")
                .withSubject(userId.toString())
                .sign(Algorithm.HMAC256(SECRET));

        User user = new User();
        user.setId(userId);
        repository.addUser(user);

        APIGatewayCustomAuthorizerEvent event = new APIGatewayCustomAuthorizerEvent();
        event.setAuthorizationToken(token);
        event.setMethodArn("arn:aws:execute-api:us-east-1:123456789012:api-id/stage/GET/resource");

        IamPolicyResponse response = authorizationUseCase.execute(event);

        assertNotNull(response);
        assertEquals(userId.toString(), response.getPrincipalId());
        Map<String, Object>[] statements = (Map<String, Object>[]) response.getPolicyDocument().get("Statement");
        assertEquals("Allow", statements[0].get("Effect"));
        assertEquals(userId.toString(), response.getContext().get("userId"));
    }
}
