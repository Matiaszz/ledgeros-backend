package com.ledgeros.mocks.repositories;

import com.ledgeros.domain.model.User;
import com.ledgeros.domain.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MockUserRepository extends UserRepository {
    private final Map<UUID, User> database = new HashMap<>();

    public void addUser(User user) {
        database.put(user.getId(), user);
    }

    @Override
    public User findById(UUID id) {
        return database.get(id);
    }
}