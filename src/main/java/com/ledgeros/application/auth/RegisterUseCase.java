package com.ledgeros.application.auth;

import com.ledgeros.domain.model.User;
import com.ledgeros.domain.repository.UserRepository;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.presentation.request.RegisterRequest;
import com.ledgeros.presentation.response.RegisterResponse;
import com.ledgeros.shared.utils.provider.HashProvider;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.ledgeros.infrastructure.exception.ExceptionCode.EMAIL_ALREADY_EXISTS;
import static com.ledgeros.infrastructure.exception.ExceptionCode.INVALID_REQUEST;

@RequiredArgsConstructor
public class RegisterUseCase {

    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();

    public RegisterUseCase() {
        this.userRepository = new UserRepository();
    }

    public RegisterResponse execute(RegisterRequest request) throws LambdaException {
        if (request == null || request.email() == null || request.email().isBlank()
                || request.password() == null || request.password().isBlank()) {
            throw new LambdaException("Email and password are required", INVALID_REQUEST);
        }

        String email = request.email().trim().toLowerCase();

        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            throw new LambdaException("User with this email already exists", EMAIL_ALREADY_EXISTS);
        }

        String hashedPassword = HashProvider.hash(request.password());
        String code = String.format("%06d", random.nextInt(1_000_000));
        Instant expiresAt = Instant.now().plus(24, ChronoUnit.HOURS);

        User newUser = User.builder()
                .id(UUID.randomUUID())
                .name(request.name() != null ? request.name().trim() : email.split("@")[0])
                .email(email)
                .password(hashedPassword)
                .emailVerified(false)
                .verificationCode(code)
                .verificationCodeExpiresAt(expiresAt)
                .build();

        userRepository.save(newUser);

        return new RegisterResponse(
                newUser.getId(),
                newUser.getName(),
                newUser.getEmail(),
                newUser.isEmailVerified(),
                newUser.getVerificationCode(),
                "User registered successfully. Please verify your email before logging in."
        );
    }
}
