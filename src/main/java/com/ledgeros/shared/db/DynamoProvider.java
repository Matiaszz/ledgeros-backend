package com.ledgeros.shared.db;


import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoProvider {
    private static final DynamoDbClient DYNAMO_CLIENT = DynamoDbClient.builder()
            .region(Region.SA_EAST_1)
            .build();
    public static final DynamoDbEnhancedClient DYNAMO = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(DYNAMO_CLIENT)
            .build();
}
