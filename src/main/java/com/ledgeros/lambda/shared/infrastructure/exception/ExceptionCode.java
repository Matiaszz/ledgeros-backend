package com.ledgeros.lambda.shared.infrastructure.exception;

public enum ExceptionCode {
    EMAIL_ALREADY_EXISTS, INTERNAL_SERVER_ERROR;

    public int getStatusCode(){
        switch (this){
            case  EMAIL_ALREADY_EXISTS:
                return 409;
            default:
                return 500;
        }
    }
}
