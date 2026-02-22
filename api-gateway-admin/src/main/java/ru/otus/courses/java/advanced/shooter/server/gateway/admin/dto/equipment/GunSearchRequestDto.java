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
        name = "GunSearchRequestDto",
        description = "Filter and pagination request for guns"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GunSearchRequestDto {

    @Schema(description = "List of IDs", example = "[10, 11, 12]")
    private List<Integer> gunIds;

    @Schema(description = "Name prefix", example = "AK")
    private String name;

    @Schema(description = "Gun type", example = "ASSAULT_RIFLE")
    private GunType type;

    @Schema(
            description = "Return guns updated after this time",
            example = "2026-02-17T12:34:56+01:00",
            format = "date-time"
    )
    private ZonedDateTime updatedAfter;

    @Schema(description = "Pagination request")
    @NotNull
    @Valid
    private PaginationRequestDto paginationRequest;
}
