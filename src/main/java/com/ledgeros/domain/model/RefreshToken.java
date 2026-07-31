package com.ledgeros.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
import java.util.UUID;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RefreshToken {
    private UUID id = UUID.randomUUID();
    private String hashToken;
    private UUID userId;
    private Instant expiresAt;
    private Instant createdAt;
    private boolean revoked;
    private Long ttl;

    @DynamoDbPartitionKey
    public UUID getId() {
        return id;
    }
}
