package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "PaginationRequestDto",
        description = "Pagination request parameters"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequestDto {

    @Schema(
            description = "Number of items per page",
            example = "20",
            minimum = "1"
    )
    @Min(value = 1, message = "Page elements count must be positive")
    @Builder.Default
    private int count = 20;

    @Schema(
            description = "Page number",
            example = "0",
            minimum = "0"
    )
    @Min(value = 0, message = "Page number must be non-negative")
    @Builder.Default
    private int page = 0;
}
