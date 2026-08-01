package com.ledgeros.domain.repository;

import com.ledgeros.domain.model.RefreshToken;
import com.ledgeros.shared.db.DynamoProvider;
import com.ledgeros.shared.db.Repository;
import com.ledgeros.shared.enums.Table;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

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

        return getTable().getItem(getKey(id));
    }

    public void revokeToken(UUID id) {
        RefreshToken refreshToken = findById(id);
        if (refreshToken != null) {
            refreshToken.setRevoked(true);
            save(refreshToken); 
        }
    }

    @Override
    public boolean delete(RefreshToken entity) {
        try {
            getTable().deleteItem(getKey(entity.getId()));
            return true;
        } catch (Exception e) {
            log.warn("Error deleting RefreshToken: {}", entity.getId(), e);
            return false;
        }
    }

    @Override
    public DynamoDbTable<RefreshToken> getTable() {
        return DynamoProvider.DYNAMO.table(
                Table.REFRESH_TOKENS.getTableName(),
                TableSchema.fromBean(RefreshToken.class)
        );
    }
}
