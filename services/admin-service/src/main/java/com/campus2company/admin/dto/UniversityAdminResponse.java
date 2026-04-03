package com.campus2company.admin.dto;

import com.campus2company.admin.model.UniversityAdminProfile;
import com.campus2company.common.model.AccountStatus;
import com.campus2company.common.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversityAdminResponse {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Long universityId;
    private Role role;
    private AccountStatus status;

    public static UniversityAdminResponse from(UniversityAdminProfile profile, AuthUserResponse auth) {
        return UniversityAdminResponse.builder()
                .id(profile.getId())
                .email(profile.getEmail())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .universityId(profile.getUniversityId())
                .role(auth.getRole())
                .status(auth.getStatus())
                .build();
    }
}
