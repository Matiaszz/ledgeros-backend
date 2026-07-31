package com.ledgeros.application.auth;

import com.ledgeros.domain.model.RefreshToken;
import com.ledgeros.domain.model.User;
import com.ledgeros.domain.repository.RefreshTokenRepository;
import com.ledgeros.domain.repository.UserRepository;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.presentation.request.RefreshTokenRequest;
import com.ledgeros.presentation.response.TokenResponse;
import com.ledgeros.shared.utils.JwtUtils;
import com.ledgeros.shared.dto.GeneratedRefreshToken;
import com.ledgeros.shared.utils.provider.HashProvider;
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

    public TokenResponse execute(RefreshTokenRequest request) throws LambdaException {
        if (request == null || request.refreshTokenId() == null) {
            throw new LambdaException("Refresh token ID is required", INVALID_REFRESH_TOKEN);
        }

        RefreshToken storedToken = refreshTokenRepository.findById(request.refreshTokenId());

        validateToken(request.refreshToken(), storedToken);

        User user = userRepository.findById(storedToken.getUserId());
        if (user == null) {
            throw new LambdaException("User not found for refresh token", UNAUTHORIZED);
        }

        // Revoke old refresh token (token rotation for security)
        refreshTokenRepository.revokeToken(storedToken.getId());

        // Issue new tokens
        String newAccessToken = jwtUtils.generateAccessToken(user.getId());
        GeneratedRefreshToken newRefreshToken = jwtUtils.generateRefreshToken(user.getId());

        return new TokenResponse(
                newAccessToken,
                newRefreshToken,
                jwtUtils.getAccessTokenExpirationInSeconds()
        );
    }

    private void validateToken(String requestRefreshToken, RefreshToken token) throws LambdaException {
        if (token == null || requestRefreshToken == null || requestRefreshToken.isEmpty()) {
            throw new LambdaException("Invalid refresh token", INVALID_REFRESH_TOKEN);
        }

        if (!HashProvider.verifyHash(requestRefreshToken, token.getHashToken())) {
            throw new LambdaException("Invalid refresh token", INVALID_REFRESH_TOKEN);
        }

        if (token.isRevoked()) {
            throw new LambdaException("Refresh token is revoked", INVALID_REFRESH_TOKEN);
        }

        if (token.getExpiresAt() != null && token.getExpiresAt().isBefore(Instant.now())) {
            throw new LambdaException("Refresh token is expired", REFRESH_TOKEN_EXPIRED);
        }
    }
}
