package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;

@Value
@Builder
public class SavedInitialPlayerInventoryItem {
    @NotNull
    InventoryEquipmentType equipmentType;

    @NotNull
    Integer equipmentId;

    @Positive
    Integer amount;

    @Min(0)
    Integer version;

    @NotNull
    @Builder.Default
    Boolean enabled = true;
}
