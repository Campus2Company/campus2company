package com.campus2company.auth.service;

import com.campus2company.auth.model.AccountStatus;
import com.campus2company.auth.model.Role;
import com.campus2company.auth.model.UserAccount;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(
                "test-secret-key-for-unit-tests-must-be-at-least-256-bits-long",
                60
        );
    }

    @Test
    void generateToken_shouldCreateValidJwt() {
        UserAccount user = createTestUser();

        String token = jwtService.generateToken(user);

        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3); // JWT has 3 parts
    }

    @Test
    void validateToken_shouldReturnClaimsForValidToken() {
        UserAccount user = createTestUser();
        String token = jwtService.generateToken(user);

        Claims claims = jwtService.validateToken(token);

        assertThat(claims).isNotNull();
        assertThat(jwtService.extractUserId(claims)).isEqualTo(user.getId());
        assertThat(jwtService.extractEmail(claims)).isEqualTo(user.getEmail());
        assertThat(jwtService.extractRole(claims)).isEqualTo(user.getRole().name());
        assertThat(jwtService.extractStatus(claims)).isEqualTo(user.getStatus().name());
    }

    @Test
    void validateToken_shouldReturnNullForInvalidToken() {
        Claims claims = jwtService.validateToken("invalid.token.here");

        assertThat(claims).isNull();
    }

    @Test
    void validateToken_shouldReturnNullForTamperedToken() {
        UserAccount user = createTestUser();
        String token = jwtService.generateToken(user);
        String tamperedToken = token.substring(0, token.length() - 5) + "xxxxx";

        Claims claims = jwtService.validateToken(tamperedToken);

        assertThat(claims).isNull();
    }

    @Test
    void getExpirySeconds_shouldReturnConfiguredValue() {
        long expirySeconds = jwtService.getExpirySeconds();

        assertThat(expirySeconds).isEqualTo(60 * 60); // 60 minutes in seconds
    }

    private UserAccount createTestUser() {
        return UserAccount.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .passwordHash("hashedpassword")
                .role(Role.STUDENT)
                .status(AccountStatus.ACTIVE)
                .build();
    }
}
