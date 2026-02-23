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
        name = "CurrencySearchRequestDto",
        description = "Filter and pagination request for currencies"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencySearchRequestDto {

    @Schema(
            description = "List of currency IDs",
            example = "[1, 2, 3]"
    )
    private List<Integer> currencyIds;

    @Schema(
            description = "Currency name prefix",
            example = "Gold"
    )
    private String name;

    @Schema(
            description = "Whether currency can be bought",
            example = "true"
    )
    private Boolean canBeBought;

    @Schema(
            description = "Currency is enabled",
            example = "false"
    )
    private Boolean enabled;

    @Schema(
            description = "Whether currency can be given as an award",
            example = "false"
    )
    private Boolean canBeGivenAsAward;

    @Schema(
            description = "Return currencies updated after given time",
            example = "2026-02-17T12:34:56+01:00"
    )
    private ZonedDateTime updatedAfter;

    @NotNull
    @Valid
    @Schema(description = "Pagination request")
    private PaginationRequestDto paginationRequest;
}
