package com.ledgeros.presentation.response;

import com.ledgeros.domain.model.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email
) {
    public UserResponse(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}
