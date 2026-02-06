package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductTradeStatus;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
@FieldNameConstants
public class ProductTradeFilterParams {

    @NotNull(message = "Player UUID must be provided")
    UUID playerUuid;

    @Builder.Default
    Set<Integer> productIds = Set.of();

    @Builder.Default
    Set<ProductTradeStatus> statuses = Set.of();

    ProductEquipmentType equipmentType;

    @Builder.Default
    Set<Integer> equipmentIds = Set.of();
}
