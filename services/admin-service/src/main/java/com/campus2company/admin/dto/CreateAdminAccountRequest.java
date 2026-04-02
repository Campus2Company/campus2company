package com.campus2company.admin.dto;

import com.campus2company.common.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdminAccountRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    /**
     * Target role: {@code UNIVERSITY_ADMIN} or {@code PLATFORM_ADMIN} (platform admins only for the latter).
     */
    @NotNull
    private Role role;
}
