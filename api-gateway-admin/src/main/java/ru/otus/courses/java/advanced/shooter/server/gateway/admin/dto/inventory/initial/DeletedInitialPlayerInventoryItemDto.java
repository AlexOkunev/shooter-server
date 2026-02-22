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
        name = "DeletedInitialPlayerInventoryItem",
        description = "Initial inventory item to delete"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeletedInitialPlayerInventoryItemDto {

    @Schema(description = "Equipment type", example = "GUN")
    @NotNull
    private EquipmentType equipmentType;

    @Schema(description = "Equipment id", example = "100")
    @NotNull
    @Min(value = 1, message = "Equipment id must be positive")
    private Integer equipmentId;
}
