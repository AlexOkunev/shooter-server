package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "processed_external_message")
@Getter
@Setter
@FieldNameConstants
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProcessedExternalMessage {

    @Id
    @Column(name = "message_uuid")
    @EqualsAndHashCode.Include
    private UUID messageUUID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "message_created_timestamp")
    private ZonedDateTime messageCreatedTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "message_processed_timestamp")
    private ZonedDateTime messageProcessedTimestamp;
}