package com.ledgeros.domain.repository;

import com.ledgeros.domain.model.RefreshToken;
import com.ledgeros.shared.db.DynamoProvider;
import com.ledgeros.shared.db.Repository;
import com.ledgeros.shared.enums.Table;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.UUID;

@Slf4j
public class RefreshTokenRepository implements Repository<RefreshToken> {

    @Override
    public RefreshToken save(RefreshToken entity) {
        getTable().putItem(entity);
        return entity;
    }

    @Override
    public RefreshToken findById(UUID id) {
        if (id == null) return null;
        return findByToken(id.toString());
    }

    public RefreshToken findByToken(String token) {
        if (token == null || token.isBlank()) return null;
        return getTable().getItem(Key.builder().addPartitionValue(token).build());
    }

    public void revokeToken(String token) {
        RefreshToken refreshToken = findByToken(token);
        if (refreshToken != null) {
            refreshToken.setRevoked(true);
            save(refreshToken);
        }
    }

    @Override
    public boolean delete(RefreshToken entity) {
        try {
            getTable().deleteItem(Key.builder().addPartitionValue(entity.getToken()).build());
            return true;
        } catch (Exception e) {
            log.warn("Error deleting RefreshToken: {}", entity.getToken(), e);
            return false;
        }
    }

    @Override
    public DynamoDbTable<RefreshToken> getTable() {
        return DynamoProvider.DYNAMO.table(
                Table.REFRESH_TOKENS.getValue(),
                TableSchema.fromBean(RefreshToken.class)
        );
    }
}
