package com.ssm.auth_service.repositories;

import com.ssm.auth_service.model.entities.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {

    @Modifying
    @Query(value = """
                UPDATE auth_service.outbox_events
                SET status = 'PROCESSING'
                WHERE id IN (
                    SELECT id
                    FROM auth_service.outbox_events
                    WHERE status = 'NEW'
                    ORDER BY created_at
                    FOR UPDATE SKIP LOCKED
                    LIMIT 100
                )
                RETURNING *
            """, nativeQuery = true)
    List<OutboxEvent> findReady();
}
