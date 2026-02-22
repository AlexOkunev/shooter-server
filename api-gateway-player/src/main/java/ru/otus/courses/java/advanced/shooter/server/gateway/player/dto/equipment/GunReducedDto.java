package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "GunReducedDto",
        description = "Reduced gun info"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GunReducedDto {

    @Schema(description = "Gun ID", example = "10")
    private int id;

    @Schema(description = "Gun is enabled", example = "true")
    private boolean enabled;

    @Schema(description = "Name", example = "AK-74")
    private String name;

    @Schema(description = "Gun weight in grams", example = "3400")
    private int weightGrams;

    @Schema(description = "Rate of fire per minute", example = "600")
    private int rateOfFirePerMinute;

    @Schema(description = "Gun type", example = "ASSAULT_RIFLE")
    private GunType type;
}