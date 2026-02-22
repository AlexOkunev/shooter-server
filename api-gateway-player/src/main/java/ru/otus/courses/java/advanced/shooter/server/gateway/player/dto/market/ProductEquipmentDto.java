package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ProductEquipment",
        description = "Product equipment wrapper",
        oneOf = {
                GunProductEquipmentDto.class,
                GrenadeProductEquipmentDto.class,
                AmmunitionProductEquipmentDto.class,
                AttachmentProductEquipmentDto.class
        }
)
public interface ProductEquipmentDto {
}
