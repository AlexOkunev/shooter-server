package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_processed_external_message_gen")
    @SequenceGenerator(name = "seq_processed_external_message_gen", sequenceName = "seq_processed_external_message", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "message_uuid")
    private UUID messageUUID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "message_created_timestamp")
    private ZonedDateTime messageCreatedTimestamp;
}