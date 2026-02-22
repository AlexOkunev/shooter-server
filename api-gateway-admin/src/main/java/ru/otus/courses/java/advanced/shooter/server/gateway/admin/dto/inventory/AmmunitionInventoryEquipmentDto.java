package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AmmunitionDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.InventoryEquipmentDto;

@Schema(name = "AmmunitionInventoryEquipment", description = "Ammunition equipment wrapper for inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmmunitionInventoryEquipmentDto implements InventoryEquipmentDto {

    @Schema(description = "Ammunition")
    private AmmunitionDto ammunition;
}
