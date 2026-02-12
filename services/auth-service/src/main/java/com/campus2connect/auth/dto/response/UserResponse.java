package com.campus2connect.auth.dto.response;

import com.campus2connect.auth.model.AccountStatus;
import com.campus2connect.auth.model.Role;
import com.campus2connect.auth.model.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UUID id;
    private String email;
    private Role role;
    private AccountStatus status;

    public static UserResponse from(UserAccount user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}
