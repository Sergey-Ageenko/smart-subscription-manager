package com.ssm.auth_service.model.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.ssm.auth_service.model.enums.OutboxStatus;
import io.lettuce.core.json.JsonType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "event_name", nullable = false, updatable = false)
    private String eventName;

    @Column(nullable = false)
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "sent_at", updatable = false)
    private LocalDateTime sentAt;
}