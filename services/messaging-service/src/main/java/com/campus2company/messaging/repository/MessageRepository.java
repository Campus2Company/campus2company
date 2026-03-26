package com.campus2company.messaging.repository;

import com.campus2company.messaging.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByConversationIdOrderBySentAtAsc(UUID conversationId);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversation.id = :conversationId AND m.senderId <> :userId AND m.read = false")
    long countUnreadInConversation(@Param("conversationId") UUID conversationId, @Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE Message m SET m.read = true WHERE m.conversation.id = :conversationId AND m.senderId <> :userId AND m.read = false")
    int markAllAsRead(@Param("conversationId") UUID conversationId, @Param("userId") UUID userId);
}
