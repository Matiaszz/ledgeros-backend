package com.ledgeros.application.auth;

import com.ledgeros.domain.model.User;
import com.ledgeros.domain.repository.UserRepository;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.presentation.request.LoginRequest;
import com.ledgeros.presentation.response.TokenResponse;
import com.ledgeros.shared.dto.GeneratedRefreshToken;
import com.ledgeros.shared.utils.JwtUtils;
import com.ledgeros.shared.utils.provider.HashProvider;
import lombok.RequiredArgsConstructor;

import static com.ledgeros.infrastructure.exception.ExceptionCode.*;

@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public LoginUseCase() {
        this.userRepository = new UserRepository();
        this.jwtUtils = new JwtUtils();
    }

    public TokenResponse execute(LoginRequest request) throws LambdaException {
        if (request == null || request.email() == null || request.email().isBlank()
                || request.password() == null || request.password().isBlank()) {
            throw new LambdaException("Email and password are required", INVALID_REQUEST);
        }

        String email = request.email().trim().toLowerCase();
        User user = userRepository.findByEmail(email);

        if (user == null || !HashProvider.verifyHash(request.password(), user.getPassword())) {
            throw new LambdaException("Invalid email or password", INVALID_CREDENTIALS);
        }

        if (!user.isEmailVerified()) {
            throw new LambdaException("Email is not verified. Please verify your email before logging in.", EMAIL_NOT_VERIFIED);
        }

        String accessToken = jwtUtils.generateAccessToken(user.getId());
        GeneratedRefreshToken refreshToken = jwtUtils.generateRefreshToken(user.getId());

        return new TokenResponse(
                accessToken,
                refreshToken,
                jwtUtils.getAccessTokenExpirationInSeconds(),
                user.getName()
        );
    }
}
