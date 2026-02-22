package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GunDto;

@Schema(name = "GunProductEquipment", description = "Product equipment: gun")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GunProductEquipmentDto implements ProductEquipmentDto {

    @Schema(description = "Gun info")
    private GunDto gun;
}
