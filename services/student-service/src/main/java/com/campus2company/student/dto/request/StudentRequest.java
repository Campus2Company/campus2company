package com.campus2company.student.dto.request;

import com.campus2company.common.enums.StudyLevel;
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
public class StudentRequest {

    private String firstName;

    private String lastName;

    @Size(max = 1000)
    private String bio;

    private String course;

    private String faculty;

    private StudyLevel studyLevel;

    private UUID universityId;
}
