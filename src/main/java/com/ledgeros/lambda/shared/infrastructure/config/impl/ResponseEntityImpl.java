package com.ledgeros.lambda.shared.infrastructure.config.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ledgeros.lambda.shared.infrastructure.config.contracts.ResponseEntity;

public class ResponseEntityImpl implements ResponseEntity {
    private APIGatewayProxyResponseEvent response;
    @Override
    public APIGatewayProxyResponseEvent createResponse() {
        if (response != null) {
            return response;
        }

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        this.setupHeaders(response);
        return response;
    }
}
