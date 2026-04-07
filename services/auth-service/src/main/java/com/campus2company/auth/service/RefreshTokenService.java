package com.campus2company.auth.service;

import com.campus2company.auth.model.RefreshToken;
import com.campus2company.auth.model.UserAccount;
import com.campus2company.auth.repository.RefreshTokenRepository;
import com.campus2company.auth.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserAccountRepository userAccountRepository;

    public String createAndStore(UUID userId) {
        String rawToken = UUID.randomUUID().toString();
        String hash = sha256(rawToken);

        RefreshToken token = RefreshToken.builder()
                .userId(userId)
                .tokenHash(hash)
                .expiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        refreshTokenRepository.save(token);

        return rawToken;
    }

    public UserAccount validateAndGetUser(String rawToken) {
        String hash = sha256(rawToken);

        RefreshToken token = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        if (token.isRevoked()) {
            // Token already used
            refreshTokenRepository.deleteAllByUserId(token.getUserId());

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Token reuse detected all sessions revoked");
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }

        return userAccountRepository.findById(token.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not found"));
    }

    public String rotate(String rawToken) {
        String hash = sha256(rawToken);

        RefreshToken old = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        old.setRevoked(true);
        refreshTokenRepository.save(old);

        return createAndStore(old.getUserId());
    }

    public void revoke(String rawToken) {
        String hash = sha256(rawToken);
        refreshTokenRepository.findByTokenHash(hash).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    public ResponseCookie buildRefreshCookie(String rawToken) {
        return ResponseCookie.from("refresh_token", rawToken)
                .httpOnly(true)
                .secure(false)// set true in production
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie buildClearCookie() {
        return ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)// set true in production
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}