package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.AmmunitionProductEquipmentDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.AttachmentProductEquipmentDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.GrenadeProductEquipmentDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.GunProductEquipmentDto;

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
