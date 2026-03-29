package com.campus2company.student.controller;

import com.campus2company.common.dto.request.CreateStudentRequest;
import com.campus2company.common.security.UserPrincipal;
import com.campus2company.student.dto.response.StudentResponse;
import com.campus2company.student.exception.StudentNotFoundException;
import com.campus2company.student.model.Student;
import com.campus2company.student.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<StudentResponse> create(@RequestBody CreateStudentRequest dto,
                                  @RequestHeader ("Authorization") String authorization) {

        StudentResponse response =  studentService.createStudent(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<StudentResponse> getMyProfile(@RequestHeader("X-Auth-User-Id") UUID authUserId) {
        return ResponseEntity.ok(studentService.getStudentByAuthId(authUserId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PatchMapping("/me")
    public ResponseEntity<StudentResponse> updateStudent(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateStudentRequest studentRequest){
        return ResponseEntity.ok(studentService.updateStudent(principal.getId(), studentRequest));
    }
}
