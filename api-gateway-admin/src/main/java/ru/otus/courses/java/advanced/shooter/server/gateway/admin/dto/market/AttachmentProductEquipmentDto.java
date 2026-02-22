package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AttachmentDto;

@Schema(name = "AttachmentProductEquipment", description = "Product equipment: attachment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentProductEquipmentDto implements ProductEquipmentDto {

    @Schema(description = "Attachment info")
    private AttachmentDto attachment;
}
