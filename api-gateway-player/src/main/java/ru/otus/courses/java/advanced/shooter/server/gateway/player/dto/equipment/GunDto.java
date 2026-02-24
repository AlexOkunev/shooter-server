package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;

import java.time.ZonedDateTime;
import java.util.List;

@Schema(
        name = "GunDto",
        description = "Gun information"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GunDto implements CacheableData<Integer> {

    @Schema(description = "Gun ID", example = "10")
    private Integer id;

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

    @Schema(description = "Compatible ammunition")
    private List<AmmunitionReducedDto> compatibleAmmunitionList;

    @Schema(description = "Compatible attachments")
    private List<AttachmentReducedDto> compatibleAttachments;

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
