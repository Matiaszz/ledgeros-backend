package com.ledgeros.application.auth;

import com.ledgeros.domain.model.User;
import com.ledgeros.domain.repository.UserRepository;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.presentation.request.VerifyEmailRequest;
import com.ledgeros.presentation.response.MessageResponse;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

import static com.ledgeros.infrastructure.exception.ExceptionCode.*;

@RequiredArgsConstructor
public class VerifyEmailUseCase {

    private final UserRepository userRepository;

    public VerifyEmailUseCase() {
        this.userRepository = new UserRepository();
    }

    public MessageResponse execute(VerifyEmailRequest request) throws LambdaException {
        if (request == null || request.email() == null || request.verificationCode() == null) {
            throw new LambdaException("Email and verification code are required", INVALID_REQUEST);
        }

        String email = request.email().trim().toLowerCase();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new LambdaException("User not found", USER_NOT_FOUND);
        }

        if (user.isEmailVerified()) {
            return new MessageResponse("Email is already verified.");
        }

        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(request.verificationCode().trim())) {
            throw new LambdaException("Invalid verification code", INVALID_VERIFICATION_CODE);
        }

        if (user.getVerificationCodeExpiresAt() != null) {
            Instant expiresAt = user.getVerificationCodeExpiresAt();
            if (Instant.now().isAfter(expiresAt)) {
                throw new LambdaException("Verification code has expired", VERIFICATION_CODE_EXPIRED);
            }
        }

        user.setEmailVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);

        return new MessageResponse("Email verified successfully. You may now log in.");
    }
}
