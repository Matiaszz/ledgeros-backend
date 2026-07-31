package com.ledgeros.application.auth;

import com.amazonaws.services.lambda.runtime.events.APIGatewayCustomAuthorizerEvent;
import com.amazonaws.services.lambda.runtime.events.IamPolicyResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ledgeros.domain.model.User;
import com.ledgeros.shared.utils.provider.SecretsProvider;
import com.ledgeros.shared.utils.UserUtils;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class AuthorizationUseCase {

    private final UserUtils userUtils;

    public IamPolicyResponse execute(APIGatewayCustomAuthorizerEvent input) {
        if (input == null || input.getAuthorizationToken() == null) {
            return generateDenyPolicy(input);
        }

        String token = input.getAuthorizationToken();
        DecodedJWT decodedJWT = decode(token);

        if (decodedJWT == null) {
            return generateDenyPolicy(input);
        }

        try {
            User user = userUtils.getUserFromJWT(decodedJWT);
            UUID userId = user.getId();
            return generatePolicy(userId.toString(), "Allow", input.getMethodArn(), Map.of("userId", userId.toString()));
        } catch (Exception e) {
            return generateDenyPolicy(input);
        }
    }

    private IamPolicyResponse generatePolicy(String principalId, String effect, String resource, Map<String, Object> contextData) {
        return IamPolicyResponse.builder()
                .withPrincipalId(principalId)
                .withPolicyDocument(
                        IamPolicyResponse.PolicyDocument.builder()
                                .withVersion(IamPolicyResponse.VERSION_2012_10_17)
                                .withStatement(Collections.singletonList(
                                        IamPolicyResponse.Statement.builder()
                                                .withAction(IamPolicyResponse.EXECUTE_API_INVOKE)
                                                .withEffect(effect)
                                                .withResource(Collections.singletonList(resource))
                                                .build()
                                ))
                                .build()
                )
                .withContext(contextData)
                .build();
    }

    private IamPolicyResponse generateDenyPolicy(APIGatewayCustomAuthorizerEvent input) {
        return generatePolicy("user", "Deny", input != null ? input.getMethodArn() : "*", null);
    }

    private DecodedJWT decode(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            String secret = SecretsProvider.getJwtSecret();
            Algorithm algorithm = Algorithm.HMAC256(secret);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("ledgeros-backend")
                    .build();
            return verifier.verify(token);

        } catch (Exception exception) {
            return null;
        }
    }

    public AuthorizationUseCase() {
        this(new UserUtils());
    }
}
