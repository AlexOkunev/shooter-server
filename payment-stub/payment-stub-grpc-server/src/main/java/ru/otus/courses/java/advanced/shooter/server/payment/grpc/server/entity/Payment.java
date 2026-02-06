package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.converter.PaymentStatusConverter;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.enumeration.PaymentStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@IdClass(PaymentId.class)
@Getter
@Setter
@FieldNameConstants
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Payment {
    @Id
    @Column(name = "player_uuid")
    @EqualsAndHashCode.Include
    private UUID playerUuid;

    @Id
    @Column(name = "payment_uuid")
    @EqualsAndHashCode.Include
    private UUID paymentUuid;

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
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @Column(name = "processing_finished_timestamp")
    private ZonedDateTime processingFinishedTimestamp;

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
