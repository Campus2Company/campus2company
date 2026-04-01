package com.campus2company.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private long expiresInSeconds;
    private UserResponse user;

    @JsonIgnore
    private String rawRefreshToken;

    public static LoginResponse of(String token, long expiresInSeconds, UserResponse user,  String rawRefreshToken) {
        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresInSeconds(expiresInSeconds)
                .user(user)
                .rawRefreshToken(rawRefreshToken)
                .build();
    }
}
