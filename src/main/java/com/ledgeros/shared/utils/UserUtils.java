package com.ledgeros.shared.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ledgeros.domain.model.User;
import com.ledgeros.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class UserUtils {
    private final UserRepository userRepository;
    public User getUserFromJWT(DecodedJWT decoded) {
        if (decoded.getSubject() == null) {
            return null;
        }
        return userRepository.findById(UUID.fromString(decoded.getSubject()));
    }

    public UserUtils(){
        this(new UserRepository());
    }
}
