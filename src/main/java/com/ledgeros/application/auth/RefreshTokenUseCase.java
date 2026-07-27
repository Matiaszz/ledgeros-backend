package com.ledgeros.application.auth;

import com.ledgeros.domain.model.RefreshToken;
import com.ledgeros.domain.model.User;
import com.ledgeros.domain.repository.RefreshTokenRepository;
import com.ledgeros.domain.repository.UserRepository;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.presentation.request.RefreshTokenRequest;
import com.ledgeros.presentation.response.TokenResponse;
import com.ledgeros.shared.utils.JwtUtils;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

import static com.ledgeros.infrastructure.exception.ExceptionCode.INVALID_REFRESH_TOKEN;
import static com.ledgeros.infrastructure.exception.ExceptionCode.REFRESH_TOKEN_EXPIRED;
import static com.ledgeros.infrastructure.exception.ExceptionCode.UNAUTHORIZED;

@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public RefreshTokenUseCase() {
        this.refreshTokenRepository = new RefreshTokenRepository();
        this.userRepository = new UserRepository();
        this.jwtUtils = new JwtUtils();
    }

    public TokenResponse execute(RefreshTokenRequest request) {
        if (request == null || request.refreshToken() == null || request.refreshToken().isBlank()) {
            throw new LambdaException("Refresh token is required", INVALID_REFRESH_TOKEN);
        }

        RefreshToken storedToken = refreshTokenRepository.findByToken(request.refreshToken());

        if (storedToken == null) {
            throw new LambdaException("Invalid refresh token", INVALID_REFRESH_TOKEN);
        }

        if (storedToken.isRevoked()) {
            throw new LambdaException("Refresh token is revoked", INVALID_REFRESH_TOKEN);
        }

        if (storedToken.getExpiresAt() != null && storedToken.getExpiresAt().isBefore(Instant.now())) {
            throw new LambdaException("Refresh token is expired", REFRESH_TOKEN_EXPIRED);
        }

        User user = userRepository.findById(storedToken.getUserId());
        if (user == null) {
            throw new LambdaException("User not found for refresh token", UNAUTHORIZED);
        }

        // Revoke old refresh token (token rotation for security)
        refreshTokenRepository.revokeToken(storedToken.getToken());

        // Issue new tokens
        String newAccessToken = jwtUtils.generateAccessToken(user.getId());
        RefreshToken newRefreshToken = jwtUtils.generateRefreshToken(user.getId());

        return new TokenResponse(
                newAccessToken,
                newRefreshToken.getToken(),
                jwtUtils.getAccessTokenExpirationInSeconds()
        );
    }
}
