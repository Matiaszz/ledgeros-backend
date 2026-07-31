package com.ledgeros.shared.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ledgeros.domain.model.RefreshToken;
import com.ledgeros.domain.repository.RefreshTokenRepository;
import com.ledgeros.shared.dto.GeneratedRefreshToken;
import com.ledgeros.shared.utils.provider.SecretsProvider;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtUtils {

    public static final long ACCESS_TOKEN_EXPIRATION_MINUTES = 15;
    public static final long REFRESH_TOKEN_EXPIRATION_DAYS = 7;

    private final RefreshTokenRepository refreshTokenRepository;

    public JwtUtils() {
        this.refreshTokenRepository = new RefreshTokenRepository();
    }

    public String generateAccessToken(UUID userId) {
        String secret = SecretsProvider.getJwtSecret();
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Instant now = Instant.now();
        Instant expiresAt = now.plus(ACCESS_TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES);

        return JWT.create()
                .withIssuer("ledgeros-backend")
                .withSubject(userId.toString())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiresAt))
                .sign(algorithm);
    }

    public GeneratedRefreshToken generateRefreshToken(UUID userId) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        Instant now = Instant.now();
        Instant expiresAt = now.plus(REFRESH_TOKEN_EXPIRATION_DAYS, ChronoUnit.DAYS);

        RefreshToken refreshToken = RefreshToken.builder()
                .hashToken(token)
                .userId(userId)
                .createdAt(now)
                .expiresAt(expiresAt)
                .revoked(false)
                .ttl(expiresAt.getEpochSecond())
                .build();

        RefreshToken saved = refreshTokenRepository.save(refreshToken);
        return new  GeneratedRefreshToken(token, saved);
    }

    public long getAccessTokenExpirationInSeconds() {
        return ACCESS_TOKEN_EXPIRATION_MINUTES * 60;
    }
}
