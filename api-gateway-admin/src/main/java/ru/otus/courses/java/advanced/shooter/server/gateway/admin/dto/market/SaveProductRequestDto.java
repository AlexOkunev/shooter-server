package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.EquipmentType;

@Schema(
        name = "SaveProductRequest",
        description = "Create/update product request (flat structure)"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveProductRequestDto {

    @Schema(description = "Equipment type", example = "GUN")
    @NotNull
    private EquipmentType equipmentType;

    @Schema(description = "Equipment id", example = "100")
    @NotNull
    private Integer equipmentId;

    @Schema(description = "Equipment amount in product", example = "1")
    @Min(value = 1, message = "Equipment amount must be positive")
    private int equipmentAmount;

    @Schema(description = "Price currency id", example = "1")
    @NotNull
    private Integer priceCurrencyId;

    @Schema(description = "Price value (amount in currency units)", example = "500")
    @Min(value = 1, message = "Price value must be positive")
    private int priceValue;

    @Schema(description = "Enabled flag", example = "true")
    @Builder.Default
    private boolean enabled = true;
}
