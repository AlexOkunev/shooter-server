package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Schema(
        name = "GrenadeDto",
        description = "Grenade information"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrenadeDto {

    @Schema(description = "Grenade ID", example = "1")
    private int id;

    @Schema(description = "Grenade is enabled", example = "true")
    private boolean enabled;

    @Schema(description = "Name", example = "Frag grenade")
    private String name;

    @Schema(description = "Blast damage radius in meters", example = "8")
    private int blastDamageRadiusMeters;

    @Schema(description = "Maximum blast damage in HP", example = "100")
    private int maxBlastDamageHp;

    @Schema(description = "Maximum blind time in milliseconds", example = "2500")
    private int maxBlindTimeMs;

    @Schema(description = "Maximum deaf time in milliseconds", example = "1500")
    private int maxDeafTimeMs;

    @Schema(
            description = "Grenade creation time with time zone",
            example = "2025-01-15T11:20:30+01:00",
            format = "date-time"
    )
    private ZonedDateTime createdTimestamp;

    @Schema(
            description = "Grenade last update time with time zone",
            example = "2025-01-16T09:10:00+01:00",
            format = "date-time"
    )
    private ZonedDateTime updatedTimestamp;
}
