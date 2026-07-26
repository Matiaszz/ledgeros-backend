package com.ledgeros.shared.config.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ledgeros.shared.config.contracts.ResponseEntity;

public class ResponseEntityImpl implements ResponseEntity {
    private APIGatewayProxyResponseEvent response;
    @Override
    public APIGatewayProxyResponseEvent createResponse() {
        if (response != null) {
            return response;
        }

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        this.setupHeaders(response);
        this.response = response;
        return response;
    }
}
