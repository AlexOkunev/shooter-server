package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.initial;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.EquipmentType;

@Schema(
        name = "SavedInitialPlayerInventoryItem",
        description = "Initial inventory item to create/update"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedInitialPlayerInventoryItemDto {

    @Schema(description = "Equipment type", example = "GUN")
    @NotNull
    private EquipmentType equipmentType;

    @Schema(description = "Equipment id", example = "100")
    @NotNull
    private Integer equipmentId;

    @Schema(description = "Amount", example = "10")
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
