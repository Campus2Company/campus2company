package com.campus2company.admin.dto;

import com.campus2company.admin.model.Lecturer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturerResponse {

    private UUID authUserId;
    private String email;
    private String firstName;
    private String lastName;
    private Instant createdAt;

    public static LecturerResponse from(Lecturer lecturer) {
        return LecturerResponse.builder()
                .authUserId(lecturer.getAuthUserId())
                .email(lecturer.getEmail())
                .firstName(lecturer.getFirstName())
                .lastName(lecturer.getLastName())
                .createdAt(lecturer.getCreatedAt())
                .build();
    }
}
