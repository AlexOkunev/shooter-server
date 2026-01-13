package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "processed_external_message")
@Data
@FieldNameConstants
public class ProcessedExternalMessage {
    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "processed_timestamp")
    private ZonedDateTime processedTimestamp;
}