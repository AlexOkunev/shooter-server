package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
    private String session;

    @NotBlank
    private String publicToken;

    @NotNull
    private UUID uuid;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime startTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime finishTimestamp;
}
