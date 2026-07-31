package com.ledgeros.mocks.repositories;

import com.ledgeros.domain.model.RefreshToken;
import com.ledgeros.domain.repository.RefreshTokenRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MockRefreshTokenRepository extends RefreshTokenRepository {
    private final Map<UUID, RefreshToken> database = new HashMap<>();

    @Override
    public RefreshToken save(RefreshToken entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        database.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public RefreshToken findById(UUID id) {
        if (id == null) return null;
        return database.get(id);
    }

    @Override
    public void revokeToken(UUID id) {
        if (id != null) {
            RefreshToken token = database.get(id);
            if (token != null) {
                token.setRevoked(true);
            }
        }
    }
}
