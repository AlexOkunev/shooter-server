package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.InventoryEquipmentDto;

@Schema(
        name = "PlayerInventoryItem",
        description = "Player inventory item"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerInventoryItemDto {

    @Schema(description = "Equipment type", example = "GUN")
    private EquipmentType equipmentType;

    @Schema(description = "Equipment details (depends on equipmentType)")
    private InventoryEquipmentDto equipment;

    @Schema(description = "Amount of equipment items in inventory", example = "3")
    private Integer amount;
}
