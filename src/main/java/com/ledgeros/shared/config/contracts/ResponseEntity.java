package com.ledgeros.shared.config.contracts;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.HashMap;
import java.util.Map;

public interface ResponseEntity {
     default void setupHeaders(APIGatewayProxyResponseEvent responseEvent) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // When running sam local start-api, AWS_SAM_LOCAL is automatically set to true by SAM CLI
        String samEnv = System.getenv("AWS_SAM_LOCAL");
        boolean isSamLocal = Boolean.getBoolean(samEnv != null ? samEnv : "false");

        String allowedOrigin = isSamLocal
                ? "*"
                : "https://ledgeros-react.vercel.app";

        headers.put("Access-Control-Allow-Origin", allowedOrigin);
        headers.put("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        responseEvent.setHeaders(headers);
    }

    APIGatewayProxyResponseEvent createResponse();
}
