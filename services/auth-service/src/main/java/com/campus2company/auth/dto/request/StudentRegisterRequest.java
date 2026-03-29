package com.campus2company.auth.dto.request;

import com.campus2company.common.enums.StudyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentRegisterRequest extends RegisterRequest{

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    @NotBlank(message = "Course is required")
    private String course;

    @NotBlank(message = "Faculty is required")
    private String faculty;

    @NotNull(message = "Study level is required")
    private StudyLevel studyLevel;

    @NotNull(message = "University ID is required")
    private Long universityId;
}
