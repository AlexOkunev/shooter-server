package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "SaveMoneyBundleRequest",
        description = "Save money bundle request"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveMoneyBundleRequestDto {

    @Schema(description = "Currency ID", example = "1")
    @NotNull
    private Integer currencyId;

    @Schema(description = "Currency amount in bundle", example = "1000")
    @Min(value = 1, message = "Currency amount must be positive")
    private int currencyAmount;

    @Schema(description = "Price in rubles", example = "199")
    @Min(value = 1, message = "Rubles price must be positive")
    private int rublesPrice;

    @Schema(description = "Enabled flag", example = "true")
    @Builder.Default
    private boolean enabled = true;
}
