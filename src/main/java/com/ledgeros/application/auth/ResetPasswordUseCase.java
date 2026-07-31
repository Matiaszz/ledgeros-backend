package com.ledgeros.application.auth;

import com.ledgeros.domain.model.User;
import com.ledgeros.domain.repository.UserRepository;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.presentation.request.ResetPasswordRequest;
import com.ledgeros.presentation.response.MessageResponse;
import com.ledgeros.shared.utils.provider.HashProvider;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

import static com.ledgeros.infrastructure.exception.ExceptionCode.*;

@RequiredArgsConstructor
public class ResetPasswordUseCase {

    private final UserRepository userRepository;

    public ResetPasswordUseCase() {
        this.userRepository = new UserRepository();
    }

    public MessageResponse execute(ResetPasswordRequest request) throws LambdaException {
        if (request == null || request.email() == null || request.resetCode() == null || request.newPassword() == null
                || request.newPassword().isBlank()) {
            throw new LambdaException("Email, reset code, and new password are required", INVALID_REQUEST);
        }

        String email = request.email().trim().toLowerCase();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new LambdaException("User not found", USER_NOT_FOUND);
        }

        if (user.getPasswordResetCode() == null || !user.getPasswordResetCode().equals(request.resetCode().trim())) {
            throw new LambdaException("Invalid password reset code", INVALID_RESET_CODE);
        }

        if (user.getPasswordResetExpiresAt() != null) {
            Instant expiresAt = user.getPasswordResetExpiresAt();
            if (Instant.now().isAfter(expiresAt)) {
                throw new LambdaException("Password reset code has expired", RESET_CODE_EXPIRED);
            }
        }

        String newHashedPassword = HashProvider.hash(request.newPassword());
        user.setPassword(newHashedPassword);
        user.setPasswordResetCode(null);
        user.setPasswordResetExpiresAt(null);

        userRepository.save(user);

        return new MessageResponse("Password reset successfully. You may now log in with your new password.");
    }
}
