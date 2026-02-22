package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GunDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.InventoryEquipmentDto;

@Schema(name = "GunInventoryEquipment", description = "Gun equipment wrapper for inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GunInventoryEquipmentDto implements InventoryEquipmentDto {

    @Schema(description = "Gun")
    private GunDto gun;
}
