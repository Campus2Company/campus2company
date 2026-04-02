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
@Table(name = "lecturers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lecturer {

    @Id
    @Column(name = "auth_user_id", nullable = false, updatable = false)
    private UUID authUserId;

    @Column(nullable = false)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
