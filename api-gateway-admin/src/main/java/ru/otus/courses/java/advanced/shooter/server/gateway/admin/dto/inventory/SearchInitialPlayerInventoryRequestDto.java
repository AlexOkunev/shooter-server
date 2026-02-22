package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationRequestDto;

@Schema(
        name = "GetInitialPlayerInventoryRequest",
        description = "Filter and pagination request for initial player inventory"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchInitialPlayerInventoryRequestDto {

    @Builder.Default
    @Schema(description = "Enabled flag filter", example = "true", nullable = true)
    private boolean enabled = true;

    @Schema(description = "Pagination request")
    @Valid
    @NotNull
    private PaginationRequestDto paginationRequest;
}
