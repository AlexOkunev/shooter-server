package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GrenadeDto;

@Schema(name = "GrenadeProductEquipment", description = "Product equipment: grenade")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrenadeProductEquipmentDto implements ProductEquipmentDto {

    @Schema(description = "Grenade info")
    private GrenadeDto grenade;
}
