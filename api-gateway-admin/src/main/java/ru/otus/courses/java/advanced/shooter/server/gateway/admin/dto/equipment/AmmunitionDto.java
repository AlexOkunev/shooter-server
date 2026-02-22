package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Schema(
        name = "AmmunitionDto",
        description = "Represents ammunition information"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmmunitionDto {

    @Schema(description = "Ammunition identifier", example = "1", format = "int32")
    private Integer id;

    @Schema(description = "Indicates if ammunition is enabled", example = "true")
    private Boolean enabled;

    @Schema(description = "Ammunition name", example = "9x19 FMJ")
    private String name;

    @Schema(description = "Bullet speed", example = "380", format = "int32")
    private Integer speed;

    @Schema(description = "Mean damage value", example = "35", format = "int32")
    private Integer damageMeanValue;

    @Schema(description = "Damage variance", example = "5", format = "int32")
    private Integer damageVariance;

    @Schema(
            description = "Compatible guns (reduced info)",
            implementation = GunReducedDto.class
    )
    private List<GunReducedDto> compatibleGuns;

    @Schema(
            description = "Creation time",
            example = "2025-01-15T11:20:30+01:00",
            format = "date-time"
    )
    private ZonedDateTime createdTimestamp;

    @Schema(
            description = "Last update time",
            example = "2025-01-15T11:20:30+01:00",
            format = "date-time"
    )
    private ZonedDateTime updatedTimestamp;
}
