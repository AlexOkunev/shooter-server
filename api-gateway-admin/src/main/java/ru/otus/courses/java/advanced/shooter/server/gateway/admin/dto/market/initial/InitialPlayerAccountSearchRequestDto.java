package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.initial;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationRequestDto;

@Schema(
        name = "InitialPlayerAccountSearchRequest",
        description = "Filter and pagination request for initial player account"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitialPlayerAccountSearchRequestDto {

    @Schema(description = "Enabled flag filter", example = "true")
    @Builder.Default
    private boolean enabled = true;

    @Schema(description = "Pagination request")
    @Valid
    @NotNull
    private PaginationRequestDto paginationRequest;
}
