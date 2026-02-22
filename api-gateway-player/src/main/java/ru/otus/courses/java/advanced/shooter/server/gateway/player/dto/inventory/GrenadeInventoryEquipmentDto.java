package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GrenadeDto;

@Schema(name = "GrenadeInventoryEquipment", description = "Grenade equipment wrapper for inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrenadeInventoryEquipmentDto implements InventoryEquipmentDto {

    @Schema(description = "Grenade")
    private GrenadeDto grenade;
}
