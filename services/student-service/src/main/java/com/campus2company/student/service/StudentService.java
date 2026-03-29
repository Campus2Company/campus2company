package com.campus2company.student.service;

import com.campus2company.common.dto.request.CreateStudentRequest;
import com.campus2company.student.dto.response.StudentResponse;
import com.campus2company.student.exception.StudentNotFoundException;
import com.campus2company.student.model.Student;
import com.campus2company.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Page<StudentResponse> getAllStudents(UUID universityId, Pageable pageable) {
        if (universityId != null) {
            return studentRepository.findByUniversityId(universityId, pageable)
                    .map(this::mapToResponse);
        }

        return studentRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public Student getStudentById(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> StudentNotFoundException.byId(id));
    }

    public StudentResponse getStudentByAuthId(UUID authUserId) {
        Student student = studentRepository.findByUserId(authUserId)
                .orElseThrow(() -> StudentNotFoundException.byAuthUserId(authUserId));

        return mapToResponse(student);
    }

    public StudentResponse createStudent(CreateStudentRequest dto) {
        Student student = Student.builder()
                .userId(dto.getAuthUserId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .bio(dto.getBio())
                .course(dto.getCourse())
                .faculty(dto.getFaculty())
                .studyLevel(dto.getStudyLevel())
                .universityId(dto.getUniversityId())
                .build();

        Student saved = studentRepository.save(student);
        log.info("Created student profile for userId: {}", dto.getAuthUserId());

        return mapToResponse(saved);
    }

    public StudentResponse updateStudent(UUID studentId, CreateStudentRequest dto) {
        Student student = getStudentById(studentId);

        if (dto.getFirstName() != null) student.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) student.setLastName(dto.getLastName());
        if (dto.getBio() != null) student.setBio(dto.getBio());

        if (dto.getCourse() != null) student.setCourse(dto.getCourse());
        if (dto.getStudyLevel() != null) student.setStudyLevel(dto.getStudyLevel());
        if (dto.getUniversityId() != null) student.setUniversityId(dto.getUniversityId());

        Student saved = studentRepository.save(student);

        return mapToResponse(saved);
    }

    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .authUserId(student.getUserId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .bio(student.getBio())
                .course(student.getCourse())
                .faculty(student.getFaculty())
                .studyLevel(student.getStudyLevel())
                .universityId(student.getUniversityId())
                .fypStatus(student.getFypStatus())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
