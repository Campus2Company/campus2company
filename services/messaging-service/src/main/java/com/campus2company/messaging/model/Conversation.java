package com.campus2company.messaging.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "conversations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"participant_one_id", "participant_two_id", "project_id"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "participant_one_id", nullable = false)
    private UUID participantOneId;

    @Column(name = "participant_two_id", nullable = false)
    private UUID participantTwoId;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(columnDefinition = "TEXT")
    private String lastMessageContent;

    private LocalDateTime lastMessageAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
