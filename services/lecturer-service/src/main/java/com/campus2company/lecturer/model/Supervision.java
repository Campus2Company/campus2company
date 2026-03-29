package com.campus2company.lecturer.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "supervisions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"lecturerId", "studentId"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supervision {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID lecturerId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime assignedAt;
}
