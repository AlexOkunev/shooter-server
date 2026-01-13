package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;

@Value
@Builder
public class DeletedInitialPlayerInventoryItem {
    @NotNull
    InventoryEquipmentType equipmentType;

    @NotNull
    Integer equipmentId;
}
