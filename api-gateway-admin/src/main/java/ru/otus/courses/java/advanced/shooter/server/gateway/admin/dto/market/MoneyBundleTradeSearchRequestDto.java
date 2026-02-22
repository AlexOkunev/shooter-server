package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundleTradeStatus;

import java.util.List;

@Schema(
        name = "MoneyBundleTradeSearchRequestDto",
        description = "Filter and pagination request for money bundle trades"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyBundleTradeSearchRequestDto {

    @Schema(description = "Filter by currency IDs", example = "[1, 2, 3]")
    private List<Integer> currencyIds;

    @Schema(description = "Filter by trade statuses", example = "[\"PAYMENT_PENDING\", \"SUCCEEDED\"]")
    private List<MoneyBundleTradeStatus> statuses;

    @Schema(description = "Pagination request")
    @NotNull
    @Valid
    private PaginationRequestDto paginationRequest;
}
