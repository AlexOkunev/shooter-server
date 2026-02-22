package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.initial;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "SavedInitialPlayerAccountItem",
        description = "Initial player account item to create/update"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedInitialPlayerAccountItemDto {

    @Schema(description = "Currency id", example = "1")
    @NotNull
    private Integer currencyId;

    @Schema(description = "Amount", example = "1000")
    @NotNull
    @Min(value = 1, message = "Amount must be positive")
    private Integer amount;

    @Schema(description = "Enabled flag", example = "true")
    @Builder.Default
    private boolean enabled = true;

    @Schema(description = "Optimistic lock version", example = "0")
    @NotNull
    private Integer version;
}
