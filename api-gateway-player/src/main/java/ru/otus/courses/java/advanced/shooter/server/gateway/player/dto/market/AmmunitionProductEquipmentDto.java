package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.AmmunitionDto;

@Schema(name = "AmmunitionProductEquipment", description = "Product equipment: ammunition")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmmunitionProductEquipmentDto implements ProductEquipmentDto {

    @Schema(description = "Ammunition info")
    private AmmunitionDto ammunition;
}
