package com.ledgeros.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private UUID id = UUID.randomUUID();
    private String name;

    private String email;
    private String password;

    @DynamoDbPartitionKey
    public UUID getId() {
        return id;
    }

}
