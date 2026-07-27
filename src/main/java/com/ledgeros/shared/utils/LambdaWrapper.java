package com.ledgeros.shared.utils;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.IamPolicyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.presentation.response.ApiResponse;
import com.ledgeros.shared.config.contracts.ResponseEntity;
import com.ledgeros.shared.config.impl.ResponseEntityImpl;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Collections;
import java.util.function.Supplier;

import static com.ledgeros.infrastructure.exception.ExceptionCode.INTERNAL_SERVER_ERROR;

@Slf4j
@NoArgsConstructor
public final class LambdaWrapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();
    private static final ResponseEntity RESPONSE_ENTITY = new ResponseEntityImpl();

    public static <T> APIGatewayProxyResponseEvent execute(Supplier<T> supplier) {
        try {
            T data = supplier.get();

            return buildResponse(
                    200,
                    ApiResponse.success(data)
            );

        } catch (LambdaException e) {
            log.warn(e.getLogMessage(), e);

            return buildResponse(
                    e.getStatusCode(),
                    ApiResponse.error(e)
            );

        } catch (Exception e) {
            log.error("Unexpected error during lambda execution", e);

            LambdaException exception = new LambdaException(
                    "Internal server error",
                    INTERNAL_SERVER_ERROR
            );

            return buildResponse(
                    500,
                    ApiResponse.error(exception)
            );
        }
    }

    public static IamPolicyResponse executeAuthorizer(Supplier<IamPolicyResponse> supplier) {
        return executeAuthorizer(supplier, "*");
    }

    public static IamPolicyResponse executeAuthorizer(Supplier<IamPolicyResponse> supplier, String methodArn) {
        try {
            return supplier.get();
        } catch (LambdaException e) {
            log.warn(e.getLogMessage(), e);
            return generateDenyPolicy(methodArn);
        } catch (Exception e) {
            log.error("Unexpected error during authorizer execution", e);
            return generateDenyPolicy(methodArn);
        }
    }

    private static IamPolicyResponse generateDenyPolicy(String resource) {
        String resourceArn = (resource != null && !resource.isBlank()) ? resource : "*";
        return IamPolicyResponse.builder()
                .withPrincipalId("user")
                .withPolicyDocument(
                        IamPolicyResponse.PolicyDocument.builder()
                                .withVersion(IamPolicyResponse.VERSION_2012_10_17)
                                .withStatement(Collections.singletonList(
                                        IamPolicyResponse.Statement.builder()
                                                .withAction(IamPolicyResponse.EXECUTE_API_INVOKE)
                                                .withEffect("Deny")
                                                .withResource(Collections.singletonList(resourceArn))
                                                .build()
                                ))
                                .build()
                )
                .build();
    }

    private static <T> APIGatewayProxyResponseEvent buildResponse(
            Integer statusCode,
            T body
    ) {

        APIGatewayProxyResponseEvent response =
                RESPONSE_ENTITY.createResponse();

        response.setStatusCode(statusCode);

        try {
            response.setBody(OBJECT_MAPPER.writeValueAsString(body));
        } catch (JsonProcessingException e) {

            log.error("Could not serialize response body.", e);

            response.setStatusCode(500);

            response.setBody("""
                    {
                      "status":"ERROR",
                      "timestamp":"%s",
                      "data":null,
                      "code":"INTERNAL_SERVER_ERROR"
                    }
                    """.formatted(Instant.now().toString()));
        }

        return response;
    }
}