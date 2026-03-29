package com.campus2company.common.dto.request;

import com.campus2company.common.enums.StudyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequest {

    @NotNull
    private UUID authUserId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String course;

    @NotNull
    private StudyLevel studyLevel;

    @NotNull
    private Long universityId;

    private String faculty;

    @Size(max = 1000)
    private String bio;
}