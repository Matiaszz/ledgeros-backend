package com.ledgeros.lambda.shared.infrastructure.exception;


import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ledgeros.lambda.model.ApiResponse;
import lombok.Getter;

@Getter
public class LambdaException extends RuntimeException {

    private final String message;
    private final ExceptionCode exceptionCode;
    private final Integer statusCode;


    public LambdaException(String message, ExceptionCode exception) {
        super(message);
        this.message = message;
        this.exceptionCode = exception;
        this.statusCode = exception.getStatusCode();
    }

    public String getLogMessage(){
        return String.format("Message: %s\nException Code: %s\nStatus Code: %d\n",
                this.message, this.exceptionCode, this.statusCode);
    }



}
