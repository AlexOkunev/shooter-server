package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PaginationRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.EquipmentType;

import java.util.List;

@Schema(
        name = "ProductTradeSearchRequestDto",
        description = "Filter and pagination request for product trades"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductTradeSearchRequestDto {

    @Schema(description = "Filter by product IDs", example = "[100, 101]")
    private List<Integer> productIds;

    @Schema(description = "Filter by equipment type", example = "GUN", nullable = true)
    private EquipmentType equipmentType;

    @Schema(description = "Filter by equipment IDs (internal ids in equipment service)", example = "[10, 11, 12]")
    private List<Integer> equipmentIds;

    @Schema(description = "Filter by trade statuses", example = "[\"SUCCEEDED\", \"FAILED\"]")
    private List<ProductTradeStatus> statuses;

    @Schema(description = "Pagination request")
    @NotNull
    @Valid
    private PaginationRequestDto paginationRequest;
}
