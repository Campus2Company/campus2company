package com.campus2company.student.dto.response;

import com.campus2company.common.enums.FypStatus;
import com.campus2company.common.enums.StudyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
        private UUID id;

        private UUID authUserId;

        private String firstName;

        private String lastName;

        private String bio;

        private String course;

        private String faculty;

        private StudyLevel studyLevel;

        private UUID universityId;

        private FypStatus fypStatus;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
}
