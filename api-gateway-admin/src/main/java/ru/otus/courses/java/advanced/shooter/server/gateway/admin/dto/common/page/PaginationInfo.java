package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "PageInfo",
        description = "Minimal pagination metadata"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationInfo {

    @Schema(
            description = "Total number of items across all pages",
            example = "12345"
    )
    private Long totalCount;

    @Schema(
            description = "Total number of pages",
            example = "124"
    )
    private Integer totalPages;

    @Schema(
            description = "Current page number",
            example = "0"
    )
    private Integer currentPageNumber;
}
