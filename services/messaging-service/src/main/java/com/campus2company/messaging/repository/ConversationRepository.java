package com.campus2company.messaging.repository;

import com.campus2company.messaging.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    @Query("SELECT c FROM Conversation c WHERE c.participantOneId = :userId OR c.participantTwoId = :userId ORDER BY c.lastMessageAt DESC NULLS LAST")
    List<Conversation> findAllByParticipant(@Param("userId") UUID userId);

    @Query("SELECT c FROM Conversation c WHERE (c.participantOneId = :userA AND c.participantTwoId = :userB) OR (c.participantOneId = :userB AND c.participantTwoId = :userA)")
    Optional<Conversation> findByParticipants(@Param("userA") UUID userA, @Param("userB") UUID userB);
}
