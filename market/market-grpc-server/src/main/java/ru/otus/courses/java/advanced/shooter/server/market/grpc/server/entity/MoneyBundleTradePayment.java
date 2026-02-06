package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Embeddable
@FieldNameConstants
public class MoneyBundleTradePayment {
    @NotBlank
    private String paymentSession;

    @NotBlank
    private String publicToken;

    @NotNull
    private UUID uuid;

    @NotNull
    private ZonedDateTime startTimestamp;

    private ZonedDateTime finishTimestamp;
}
