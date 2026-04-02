package com.campus2company.admin.dto;

import com.campus2company.common.model.AccountStatus;
import com.campus2company.common.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserResponse {

    private UUID id;
    private String email;
    private Role role;
    private AccountStatus status;
}
