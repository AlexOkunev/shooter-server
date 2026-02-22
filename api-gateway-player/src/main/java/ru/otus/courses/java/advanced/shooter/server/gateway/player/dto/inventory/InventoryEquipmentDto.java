package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.inventory;


import io.swagger.v3.oas.annotations.media.Schema;

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
