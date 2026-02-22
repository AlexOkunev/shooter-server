package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.initial;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencyDto;

import java.time.ZonedDateTime;

@Schema(
        name = "InitialPlayerAccountItem",
        description = "Initial player account item"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitialPlayerAccountItemDto {

    @Schema(description = "Currency")
    private CurrencyDto currency;

    @Schema(description = "Amount", example = "1000")
    private Integer amount;

    @Schema(description = "Enabled", example = "true")
    private boolean enabled;

    @Schema(description = "Created timestamp", example = "2025-01-15T11:20:30+01:00")
    private ZonedDateTime createdTimestamp;

    @Schema(description = "Updated timestamp", example = "2025-01-15T11:20:30+01:00")
    private ZonedDateTime updatedTimestamp;

    @Schema(description = "Optimistic lock version", example = "0")
    private Integer version;
}
