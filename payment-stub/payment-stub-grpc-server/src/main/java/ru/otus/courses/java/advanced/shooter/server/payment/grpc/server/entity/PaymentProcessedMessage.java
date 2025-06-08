package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.converter.PaymentStatusConverter;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.enumeration.PaymentStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "payment_processed_message_outbox")
@FieldNameConstants
public class PaymentProcessedMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_payment_processed_message_gen")
    @SequenceGenerator(name = "seq_payment_processed_message_gen", sequenceName = "seq_payment_processed_message", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "message_uuid")
    private UUID messageUuid;

    @NotNull
    @Column(name = "trade_uuid")
    private UUID tradeUuid;

    @NotNull
    @Column(name = "payment_uuid")
    private UUID paymentUuid;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "processing_finished_timestamp")
    private ZonedDateTime processingFinishedTimestamp;

    @NotNull
    @Column(name = "status")
    @Convert(converter = PaymentStatusConverter.class)
    private PaymentStatus status;
}
