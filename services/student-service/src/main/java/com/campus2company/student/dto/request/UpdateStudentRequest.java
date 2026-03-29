package com.campus2company.student.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentRequest {

    private String firstName;

    private String lastName;

    @Size(max = 1000)
    private String bio;
}
