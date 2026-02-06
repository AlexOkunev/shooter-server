package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Value
@Builder
@FieldNameConstants
public class PlayerCurrencyOperationCommand {

    @NotNull
    UUID playerUuid;

    @NotNull
    @Positive
    Integer currencyId;

    @NotNull
    @Positive
    Integer amount;
}
