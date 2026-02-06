package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;

import java.util.Set;

@Value
@Builder
@FieldNameConstants
public class ProductFilterParams {

    Boolean enabled;
    Boolean equipmentEnabled;
    Boolean priceCurrencyEnabled;

    @Builder.Default
    Set<ProductEquipmentType> equipmentTypes = Set.of();

    @Builder.Default
    Set<Integer> priceCurrencyIds = Set.of();
}
