package com.ledgeros.shared.db;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

public class DynamoProvider {
    private static final String REGION_NAME = System.getenv().getOrDefault("AWS_REGION",
            System.getenv().getOrDefault("AWS_DEFAULT_REGION", "us-east-1"));

    private static final DynamoDbClient DYNAMO_CLIENT = createClient();

    private static DynamoDbClient createClient() {
        DynamoDbClientBuilder builder = DynamoDbClient.builder()
                .region(Region.of(REGION_NAME));

        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");

        if (accessKey != null && !accessKey.isBlank() && secretKey != null && !secretKey.isBlank()) {
            builder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey.trim(), secretKey.trim())
            ));
        }

        return builder.build();
    }

    public static final DynamoDbEnhancedClient DYNAMO = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(DYNAMO_CLIENT)
            .build();
}
