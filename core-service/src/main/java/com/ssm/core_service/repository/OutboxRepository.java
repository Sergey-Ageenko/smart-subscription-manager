package com.ssm.core_service.repository;

import com.ssm.core_service.model.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {

    @Modifying
    @Query(value = """
                UPDATE core_service.outbox_events
                SET status = 'PROCESSING'
                WHERE id IN (
                    SELECT id
                    FROM core_service.outbox_events
                    WHERE status = 'NEW'
                    ORDER BY created_at
                    FOR UPDATE SKIP LOCKED
                    LIMIT 100
                )
                RETURNING *
            """, nativeQuery = true)
    List<OutboxEvent> findReady();

    @Modifying
    @Query(value = """
                UPDATE core_service.outbox_events
                SET status = 'NEW',
                    processing_started_at = NULL
                WHERE id IN (
                    SELECT id
                    FROM core_service.outbox_events
                    WHERE status = 'PROCESSING'
                    AND processing_started_at < :threshold
                    ORDER BY processing_started_at
                    FOR UPDATE SKIP LOCKED
                    LIMIT 100
                )
                RETURNING *
            """, nativeQuery = true)
    List<OutboxEvent> findStuckProcessingEvents(@Param("threshold") LocalDateTime threshold);
}
