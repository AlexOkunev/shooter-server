package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AttachmentDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.InventoryEquipmentDto;

@Schema(name = "AttachmentInventoryEquipment", description = "Attachment equipment wrapper for inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentInventoryEquipmentDto implements InventoryEquipmentDto {

    @Schema(description = "Attachment")
    private AttachmentDto attachment;
}
