package com.campus2company.admin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "platform_admin_profiles",
        uniqueConstraints = @jakarta.persistence.UniqueConstraint(
                name = "uk_platform_admin_profiles_email",
                columnNames = "email"
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformAdminProfile {

    /** Same UUID as auth user_accounts.id */
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "university_id", nullable = false)
    private Long universityId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
