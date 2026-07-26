package com.ledgeros.infrastructure.exception;


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
