package com.campus2company.student.repository;

import com.campus2company.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    List<Student> findByUniversityId(Long universityId);
    Optional<Student> findByUserId(UUID authUserId);
    List<Student> findByCourse(String course);
}
