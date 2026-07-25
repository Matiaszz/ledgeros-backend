package com.ledgeros.lambda.shared.config.contracts;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.HashMap;
import java.util.Map;

public interface ResponseEntity {
     default void setupHeaders(APIGatewayProxyResponseEvent responseEvent) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        responseEvent.setHeaders(headers);
    }

    APIGatewayProxyResponseEvent createResponse();
}
