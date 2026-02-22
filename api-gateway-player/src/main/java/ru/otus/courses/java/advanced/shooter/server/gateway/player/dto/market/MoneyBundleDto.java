package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.CurrencyDto;

import java.time.ZonedDateTime;

@Schema(
        name = "MoneyBundle",
        description = "Money bundle information"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyBundleDto {

    @Schema(description = "ID", example = "1")
    private Integer id;

    @Schema(description = "Currency")
    private CurrencyDto currency;

    @Schema(description = "Amount of currency in bundle", example = "1000")
    private Integer currencyAmount;

    @Schema(description = "Price in rubles", example = "199")
    private Integer rublesPrice;

    @Schema(description = "Enabled", example = "true")
    private Boolean enabled;

    @Schema(description = "Created timestamp")
    private ZonedDateTime createdTimestamp;

    @Schema(description = "Updated timestamp")
    private ZonedDateTime updatedTimestamp;

    @Schema(description = "Version", example = "3")
    private Integer version;
}
