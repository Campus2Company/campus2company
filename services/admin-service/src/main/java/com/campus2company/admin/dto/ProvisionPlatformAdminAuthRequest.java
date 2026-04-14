package com.campus2company.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Body for auth-service POST /admin/university-admins
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionPlatformAdminAuthRequest {

    private UUID id;
    private String email;
    private String password;
}
