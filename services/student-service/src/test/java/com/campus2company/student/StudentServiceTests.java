package com.campus2company.student;

import com.campus2company.student.model.Student;
import com.campus2company.student.repository.StudentRepository;
import com.campus2company.student.service.StudentService;
import com.campus2company.common.enums.FypStatus;
import com.campus2company.common.enums.StudyLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTests {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void getStudentByIdWhenExistsReturnsStudent() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Student student = Student.builder()
                .id(id)
                .userId(userId)
                .firstName("John")
                .lastName("Doe")
                .bio("Bio")
                .course("Computer Science")
                .faculty("Engineering")
                .studyLevel(StudyLevel.UNDERGRADUATE)
                .universityId(UUID.randomUUID())
                .fypStatus(FypStatus.AVAILABLE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(id);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
    }

    @Test
    void getStudentByIdWhenNotFoundThrowsException() {
        UUID id = UUID.randomUUID();
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.getStudentById(id));
    }
}
