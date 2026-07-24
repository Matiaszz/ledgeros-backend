package com.ledgeros.lambda.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledgeros.lambda.model.ApiResponse;
import com.ledgeros.lambda.model.TransactionRecord;
import com.ledgeros.lambda.shared.infrastructure.config.contracts.ResponseEntity;
import com.ledgeros.lambda.shared.infrastructure.config.impl.LambdaWrapper;
import com.ledgeros.lambda.shared.infrastructure.config.impl.ResponseEntityImpl;

import java.math.BigDecimal;

/**
 * Firebase-like Lambda Function: CreateTransaction
 */
public class CreateTransactionFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {

        return LambdaWrapper.execute(() -> new TransactionRecord(
            "tx-9999",
            "Nova Transação Criada via Firebase-like Function",
            new BigDecimal("350.00"),
            "BRL",
            "Receita")
        );
    }
}
