package com.campus2company.lecturer.repository;

import com.campus2company.lecturer.model.Supervision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupervisionRepository extends JpaRepository<Supervision, UUID> {

    List<Supervision> findByLecturerIdAndActiveTrue(UUID lecturerId);

    Optional<Supervision> findByLecturerIdAndStudentId(UUID lecturerId, UUID studentId);

    boolean existsByLecturerIdAndStudentIdAndActiveTrue(UUID lecturerId, UUID studentId);
}
