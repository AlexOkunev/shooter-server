package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.outbox;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.converter.PaymentStatusConverter;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.enumeration.PaymentStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_processed_message_outbox")
@IdClass(PaymentProcessedMessageId.class)
@Getter
@Setter
@FieldNameConstants
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PaymentProcessedMessage {

    @Id
    @Column(name = "player_uuid")
    @EqualsAndHashCode.Include
    private UUID playerUuid;

    @Id
    @Column(name = "uuid")
    @EqualsAndHashCode.Include
    private UUID uuid;

    @NotNull
    @Column(name = "trade_uuid")
    private UUID tradeUuid;

    @NotNull
    @Column(name = "payment_uuid")
    private UUID paymentUuid;

    @NotNull
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @NotNull
    @Column(name = "processing_finished_timestamp")
    private ZonedDateTime processingFinishedTimestamp;

    @NotNull
    @Column(name = "status")
    @Convert(converter = PaymentStatusConverter.class)
    private PaymentStatus status;
}
