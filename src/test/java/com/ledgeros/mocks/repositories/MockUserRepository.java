package com.ledgeros.mocks.repositories;

import com.ledgeros.domain.model.User;
import com.ledgeros.domain.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MockUserRepository extends UserRepository {
    private final Map<UUID, User> database = new HashMap<>();

    public void addUser(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        database.put(user.getId(), user);
    }

    @Override
    public User save(User entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        database.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public User findById(UUID id) {
        return database.get(id);
    }

    @Override
    public User findByEmail(String email) {
        if (email == null) return null;
        String cleanEmail = email.trim().toLowerCase();
        return database.values().stream()
                .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(cleanEmail))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean delete(User entity) {
        if (entity != null && entity.getId() != null) {
            return database.remove(entity.getId()) != null;
        }
        return false;
    }
}