package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory;


import io.swagger.v3.oas.annotations.media.Schema;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.GrenadeInventoryEquipmentDto;

@Schema(
        name = "InventoryEquipment",
        description = "Inventory equipment wrapper",
        oneOf = {
                GunInventoryEquipmentDto.class,
                GrenadeInventoryEquipmentDto.class,
                AmmunitionInventoryEquipmentDto.class,
                AttachmentInventoryEquipmentDto.class
        }
)
public interface InventoryEquipmentDto {
}
