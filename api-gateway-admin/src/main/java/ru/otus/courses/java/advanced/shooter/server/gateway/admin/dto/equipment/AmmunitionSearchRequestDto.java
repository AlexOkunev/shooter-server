package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationRequestDto;

import java.time.ZonedDateTime;
import java.util.List;

@Schema(
        name = "AmmunitionSearchRequestDto",
        description = "Filter and pagination request for ammunition"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmmunitionSearchRequestDto {

    @Schema(description = "Ammunition IDs", example = "[1, 2, 3]")
    private List<Integer> ammunitionIds;

    @Schema(description = "Name prefix", example = "9x19")
    private String name;

    @Schema(description = "Compatible gun IDs", example = "[10, 11]")
    private List<Integer> compatibleGunIds;

    @Schema(description = "Enabled", example = "true")
    private Boolean enabled;

    @Schema(description = "Filter by compatible guns that must enabled", example = "true")
    private Boolean onlyEnabledCompatibleGuns;

    @Schema(
            description = "Return ammunition updated after this time",
            example = "2026-02-17T12:34:56+01:00",
            format = "date-time"
    )
    private ZonedDateTime updatedAfter;

    @Schema(description = "Pagination request")
    @NotNull
    @Valid
    private PaginationRequestDto paginationRequest;
}
