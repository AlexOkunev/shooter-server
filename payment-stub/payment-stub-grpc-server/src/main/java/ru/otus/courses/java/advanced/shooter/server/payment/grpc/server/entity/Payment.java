package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.converter.PaymentStatusConverter;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.enumeration.PaymentStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "payment")
@FieldNameConstants
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_payment_gen")
    @SequenceGenerator(name = "seq_payment_gen", sequenceName = "seq_payment", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "player_id")
    private Integer playerId;

    @NotBlank
    @Column(name = "player_email")
    private String playerEmail;

    @NotNull
    @Column(name = "trade_uuid")
    private UUID tradeUuid;

    @Positive
    @Column(name = "rubles_amount")
    private Integer rublesAmount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "processing_finished_timestamp")
    private ZonedDateTime processingFinishedTimestamp;

    @NotNull
    @Column(name = "payment_uuid")
    private UUID paymentUuid;

    @NotBlank
    @Column(name = "payment_session")
    private String paymentSession;

    @NotBlank
    @Column(name = "public_token")
    private String publicToken;

    @NotNull
    @Column(name = "status")
    @Convert(converter = PaymentStatusConverter.class)
    private PaymentStatus status;
}
