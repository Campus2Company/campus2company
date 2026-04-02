package com.campus2company.admin.repository;

import com.campus2company.admin.model.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LecturerRepository extends JpaRepository<Lecturer, UUID> {
}
