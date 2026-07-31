package com.ledgeros.domain.repository;

import com.ledgeros.domain.model.User;
import com.ledgeros.shared.db.DynamoProvider;
import com.ledgeros.shared.db.Repository;
import com.ledgeros.shared.enums.Table;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.UUID;

@Slf4j
public class UserRepository implements Repository<User> {
    @Override
    public User save(User entity) {
        getTable().putItem(entity);
        return entity;
    }

    @Override
    public User findById(UUID id) {
        if (id == null) return null;
        return getTable().getItem(getKey(id));
    }

    public User findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        String targetEmail = email.trim().toLowerCase();
        try {
            return getTable().scan(
                    ScanEnhancedRequest.builder()
                            .filterExpression(Expression.builder()
                                    .expression("email = :email")
                                    .putExpressionValue(":email", AttributeValue.builder().s(targetEmail).build())
                                    .build())
                            .build()
            ).items().stream().findFirst().orElse(null);
        } catch (Exception e) {
            log.error("Error scanning user by email: {}", targetEmail, e);
            return null;
        }
    }

    @Override
    public boolean delete(User entity) {
        try {
            if (entity != null && entity.getId() != null) {
                getTable().deleteItem(getKey(entity.getId()));
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("Error on deleting User: {}", entity != null ? entity.getId() : "null");
            return false;
        }
    }

    @Override
    public DynamoDbTable<User> getTable() {
        return DynamoProvider.DYNAMO.table(
                Table.USERS.getTableName(),
                TableSchema.fromBean(User.class)
        );
    }
}
