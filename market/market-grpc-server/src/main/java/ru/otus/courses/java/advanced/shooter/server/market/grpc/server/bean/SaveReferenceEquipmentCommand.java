package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;

@Value
@Builder
@FieldNameConstants
public class SaveReferenceEquipmentCommand {

    @NotNull
    Integer equipmentId;

    @NotNull
    ProductEquipmentType equipmentType;

    @NotNull
    String name;

    @NotNull
    @Builder.Default
    boolean enabled = true;
}
