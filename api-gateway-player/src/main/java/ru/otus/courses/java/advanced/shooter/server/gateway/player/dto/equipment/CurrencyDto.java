package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Schema(
        name = "CurrencyDto",
        description = "In-game currency information"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {

    @Schema(
            description = "Currency ID",
            example = "1"
    )
    private Integer id;

    @Schema(
            description = "Indicates if currency is enabled",
            example = "true"
    )
    @Builder.Default
    private boolean enabled = true;

    @Schema(
            description = "Currency name",
            example = "Gold"
    )
    private String name;

    @Schema(
            description = "Whether currency can be bought",
            example = "true"
    )
    @Builder.Default
    private boolean canBeBought = true;

    @Schema(
            description = "Whether currency can be given as an award",
            example = "true"
    )
    @Builder.Default
    private boolean canBeGivenAsAward = true;

    @Schema(
            description = "Currency creation time with time zone",
            example = "2025-01-15T11:20:30+01:00",
            format = "date-time"
    )
    private ZonedDateTime createdTimestamp;

    @Schema(
            description = "Currency last update time with time zone",
            example = "2025-01-16T09:10:00+01:00",
            format = "date-time"
    )
    private ZonedDateTime updatedTimestamp;
}
