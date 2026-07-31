package com.ledgeros.application.auth;

import com.ledgeros.domain.model.User;
import com.ledgeros.domain.repository.UserRepository;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.presentation.request.ForgotPasswordRequest;
import com.ledgeros.presentation.response.ForgotPasswordResponse;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.ledgeros.infrastructure.exception.ExceptionCode.INVALID_REQUEST;
import static com.ledgeros.infrastructure.exception.ExceptionCode.USER_NOT_FOUND;

@RequiredArgsConstructor
public class ForgotPasswordUseCase {

    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();

    public ForgotPasswordUseCase() {
        this.userRepository = new UserRepository();
    }

    public ForgotPasswordResponse execute(ForgotPasswordRequest request) throws LambdaException {
        if (request == null || request.email() == null || request.email().isBlank()) {
            throw new LambdaException("Email is required", INVALID_REQUEST);
        }

        String email = request.email().trim().toLowerCase();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new LambdaException("User not found with provided email", USER_NOT_FOUND);
        }

        String resetCode = String.format("%06d", random.nextInt(1_000_000));
        Instant expiresAt = Instant.now().plus(30, ChronoUnit.MINUTES);

        user.setPasswordResetCode(resetCode);
        user.setPasswordResetExpiresAt(expiresAt);

        userRepository.save(user);

        return new ForgotPasswordResponse(
                user.getEmail(),
                "Password reset code generated. Use it within 30 minutes to set a new password.",
                resetCode
        );
    }
}
