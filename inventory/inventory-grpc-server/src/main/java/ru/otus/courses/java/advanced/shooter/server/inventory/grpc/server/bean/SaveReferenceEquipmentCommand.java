package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;

@Value
@Builder
@FieldNameConstants
public class SaveReferenceEquipmentCommand {

    @NotNull
    Integer equipmentId;

    @NotNull
    InventoryEquipmentType equipmentType;

    @NotNull
    String name;

    @NotNull
    @Builder.Default
    boolean enabled = true;
}
