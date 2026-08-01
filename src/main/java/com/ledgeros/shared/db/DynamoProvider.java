package com.ledgeros.shared.db;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoProvider {
    private static final String REGION_NAME = System.getenv().getOrDefault("AWS_REGION",
            System.getenv().getOrDefault("AWS_DEFAULT_REGION", "us-east-1"));

    private static final DynamoDbClient DYNAMO_CLIENT = createClient();

    private static DynamoDbClient createClient() {
        return DynamoDbClient.builder()
                .region(Region.of(REGION_NAME))
                .build();
    }

    public static final DynamoDbEnhancedClient DYNAMO = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(DYNAMO_CLIENT)
            .build();
}
