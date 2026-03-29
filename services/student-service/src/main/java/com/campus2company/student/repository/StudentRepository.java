package com.campus2company.student.repository;

import com.campus2company.student.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Page<Student> findByUniversityId(Long universityId, Pageable pageable);
    Optional<Student> findByUserId(UUID authUserId);
    Page<Student> findByCourse(String course, Pageable pageable);
}
