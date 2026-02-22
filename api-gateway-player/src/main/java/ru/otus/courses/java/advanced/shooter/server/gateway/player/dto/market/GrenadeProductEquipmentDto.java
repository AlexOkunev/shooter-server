package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GrenadeDto;

@Schema(name = "GrenadeProductEquipment", description = "Product equipment: grenade")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrenadeProductEquipmentDto implements ProductEquipmentDto {

    @Schema(description = "Grenade info")
    private GrenadeDto grenade;
}
