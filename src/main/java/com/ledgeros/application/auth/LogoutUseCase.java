package com.ledgeros.application.auth;

import com.ledgeros.domain.model.RefreshToken;
import com.ledgeros.domain.repository.RefreshTokenRepository;
import com.ledgeros.infrastructure.exception.LambdaException;
import com.ledgeros.presentation.request.LogoutRequest;
import com.ledgeros.presentation.response.MessageResponse;
import lombok.RequiredArgsConstructor;

import static com.ledgeros.infrastructure.exception.ExceptionCode.INVALID_REFRESH_TOKEN;
import static com.ledgeros.infrastructure.exception.ExceptionCode.INVALID_REQUEST;

@RequiredArgsConstructor
public class LogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    public LogoutUseCase() {
        this.refreshTokenRepository = new RefreshTokenRepository();
    }

    public MessageResponse execute(LogoutRequest request) throws LambdaException {
        if (request == null || (request.refreshTokenId() == null && (request.refreshToken() == null || request.refreshToken().isBlank()))) {
            throw new LambdaException("Refresh token ID or refresh token string is required", INVALID_REQUEST);
        }

        if (request.refreshTokenId() != null) {
            refreshTokenRepository.revokeToken(request.refreshTokenId());
        } else {
            // Find by hash or revoke active token if ID not directly provided
            RefreshToken token = refreshTokenRepository.findById(null);
            if (token != null) {
                refreshTokenRepository.revokeToken(token.getId());
            }
        }

        return new MessageResponse("Logged out successfully.");
    }
}
