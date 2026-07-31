package com.ledgeros.domain.model;

import com.ledgeros.presentation.request.RegisterRequest;
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
public class User {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private String name;

    private String email;
    private String password;
    private boolean emailVerified;

    private String verificationCode;
    private Instant verificationCodeExpiresAt;

    private String passwordResetCode;
    private Instant passwordResetExpiresAt;

    @DynamoDbPartitionKey
    public UUID getId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        return this.id;
    }

    public User(RegisterRequest req, String hashPassword) {
        this.id = UUID.randomUUID();
        this.name = req.name();
        this.email = req.email();
        this.password = hashPassword;
    }
}
