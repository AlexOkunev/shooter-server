package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.AmmunitionDto;

@Schema(name = "AmmunitionInventoryEquipment", description = "Ammunition equipment wrapper for inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmmunitionInventoryEquipmentDto implements InventoryEquipmentDto {

    @Schema(description = "Ammunition")
    private AmmunitionDto ammunition;
}
