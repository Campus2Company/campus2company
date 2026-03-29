package com.campus2company.student.exception;

import java.util.UUID;

public class StudentNotFoundException extends RuntimeException {
    private StudentNotFoundException(String message) {
        super(message);
    }

    public static StudentNotFoundException byId(UUID id) {
        return new StudentNotFoundException("Student not found with id: " + id);
    }

    public static StudentNotFoundException byAuthUserId(UUID authUserId) {
        return new StudentNotFoundException("Student not found with userId: " + authUserId);
    }
}
