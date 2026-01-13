package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;

import java.util.UUID;

@Value
@Builder
@FieldNameConstants
public class PlayerEquipmentOperationCommand {

    @NotNull
    UUID playerUuid;

    @NotNull
    InventoryEquipmentType equipmentType;

    @NotNull
    Integer equipmentId;

    @Positive
    @NotNull
    Integer amount;
}
