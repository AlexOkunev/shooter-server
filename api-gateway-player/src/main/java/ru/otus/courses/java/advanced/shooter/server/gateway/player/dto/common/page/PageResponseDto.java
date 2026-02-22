package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(
        name = "PageResponse",
        description = "Generic paged response"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto<T> {

    @Schema(description = "Page items")
    private List<T> items;

    @Schema(description = "Pagination information")
    private PaginationInfo paginationInfo;
}
