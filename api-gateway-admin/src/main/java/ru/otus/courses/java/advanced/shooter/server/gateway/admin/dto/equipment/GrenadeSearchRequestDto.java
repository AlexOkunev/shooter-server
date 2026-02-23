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
        name = "GrenadeSearchRequestDto",
        description = "Filter and pagination request for grenades"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrenadeSearchRequestDto {

    @Schema(description = "List of grenade IDs", example = "[1, 2, 3]")
    private List<Integer> grenadeIds;

    @Schema(description = "Grenade name prefix", example = "Frag")
    private String name;

    @Schema(description = "Enabled", example = "true")
    private Boolean enabled;

    @Schema(
            description = "Return grenades updated after given time",
            example = "2026-02-17T12:34:56+01:00"
    )
    private ZonedDateTime updatedAfter;

    @Schema(description = "Pagination request")
    @NotNull
    @Valid
    private PaginationRequestDto paginationRequest;
}
