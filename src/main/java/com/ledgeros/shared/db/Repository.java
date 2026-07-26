package com.ledgeros.shared.db;

import com.ledgeros.shared.enums.Table;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.UUID;

public interface Repository<T> {

    T save(T entity);

    T findById(UUID id);

    boolean delete(T entity);

    DynamoDbTable<T> getTable();

    default Key getKey(UUID id) {
        return Key.builder().addPartitionValue(id).build();
    }

}
