package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
@FieldNameConstants
public class MoneyBundleTradeFilterParams {

    @NotNull(message = "Player UUID must be provided")
    UUID playerUuid;

    @Builder.Default
    Set<Integer> currencyIds = Set.of();

    @Builder.Default
    Set<MoneyBundleTradeStatus> statuses = Set.of();
}
