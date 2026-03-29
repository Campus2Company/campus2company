package com.campus2company.lecturer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateLecturerProfileRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Department is required")
    @Size(max = 200, message = "Department must not exceed 200 characters")
    private String department;

    @Size(max = 200, message = "Faculty must not exceed 200 characters")
    private String faculty;

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;

    @Size(max = 500, message = "Specializations must not exceed 500 characters")
    private String specializations;

    @NotNull(message = "University ID is required")
    private Long universityId;
}
