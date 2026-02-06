package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;

@Value
@Builder
@FieldNameConstants
public class ProductSavedData {

    @NotNull
    ProductEquipmentType equipmentType;

    @NotNull
    Integer equipmentId;

    @Positive
    @NotNull
    Integer equipmentAmount;

    @Positive
    @NotNull
    Integer priceCurrencyId;

    @Positive
    @NotNull
    Integer priceCurrencyAmount;

    @Builder.Default
    boolean enabled = true;
}
