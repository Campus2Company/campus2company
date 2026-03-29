package com.campus2company.student.controller;

import com.campus2company.common.dto.request.CreateStudentRequest;
import com.campus2company.common.security.UserPrincipal;
import com.campus2company.student.dto.response.StudentResponse;
import com.campus2company.student.model.Student;
import com.campus2company.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/createStudent")
    public ResponseEntity<StudentResponse> createStudent(
            @Valid @RequestBody CreateStudentRequest dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.createStudent(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<StudentResponse> getMyProfile(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(studentService.getStudentByAuthId(principal.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<StudentResponse>> getAllStudents(
            @RequestParam(required = false) UUID universityId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC ) Pageable pageable
    ) {
        return ResponseEntity.ok(studentService.getAllStudents(universityId, pageable));
    }

    @PatchMapping("/me")
    public ResponseEntity<StudentResponse> updateStudent(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateStudentRequest studentRequest
    ) {
        return ResponseEntity.ok(studentService.updateStudent(principal.getId(), studentRequest));
    }
}
