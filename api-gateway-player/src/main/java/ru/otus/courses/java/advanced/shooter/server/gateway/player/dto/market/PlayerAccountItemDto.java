package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.CurrencyDto;

@Schema(
        name = "PlayerAccountItem",
        description = "Player account item"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAccountItemDto {

    @Schema(description = "Currency")
    private CurrencyDto currency;

    @Schema(description = "Amount of equipment items in inventory", example = "3")
    private Integer amount;
}
