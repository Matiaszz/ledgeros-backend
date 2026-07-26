package com.ledgeros.domain.repository;

import com.ledgeros.domain.model.User;
import com.ledgeros.shared.db.DynamoProvider;
import com.ledgeros.shared.db.Repository;
import com.ledgeros.shared.enums.Table;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

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
        return getTable().getItem(getKey(id));
    }

    @Override
    public boolean delete(User entity) {
        try{
            getTable().deleteItem(getKey(entity.getId()));
            return true;
        }catch (Exception e){
            log.warn("Error on deleting User: {}", entity.getId());
            return false;
        }
    }

    @Override
    public DynamoDbTable<User> getTable() {
        return DynamoProvider.DYNAMO.table(
                Table.USERS.getValue(),
                TableSchema.fromBean(User.class)
        );
    }
}
