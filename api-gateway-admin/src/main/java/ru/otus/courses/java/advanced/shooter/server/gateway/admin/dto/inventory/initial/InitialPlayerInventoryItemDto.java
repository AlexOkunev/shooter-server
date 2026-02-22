package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.initial;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.InventoryEquipmentDto;

import java.time.ZonedDateTime;

@Schema(
        name = "InitialPlayerInventoryItem",
        description = "Initial player inventory item"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitialPlayerInventoryItemDto {

    @Schema(description = "Equipment details")
    private InventoryEquipmentDto equipment;

    @Schema(description = "Equipment type", example = "GUN")
    private EquipmentType equipmentType;

    @Schema(description = "Amount", example = "10")
    private Integer amount;

    @Schema(description = "Enabled", example = "true")
    private boolean enabled;

    @Schema(description = "Created timestamp", example = "2025-01-15T11:20:30+01:00")
    private ZonedDateTime createdTimestamp;

    @Schema(description = "Updated timestamp", example = "2025-01-15T11:20:30+01:00")
    private ZonedDateTime updatedTimestamp;

    @Schema(description = "Optimistic lock version", example = "1")
    private Integer version;
}
