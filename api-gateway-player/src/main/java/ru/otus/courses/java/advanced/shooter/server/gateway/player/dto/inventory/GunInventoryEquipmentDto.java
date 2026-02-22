package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GunDto;

@Schema(name = "GunInventoryEquipment", description = "Gun equipment wrapper for inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GunInventoryEquipmentDto implements InventoryEquipmentDto {

    @Schema(description = "Gun")
    private GunDto gun;
}
