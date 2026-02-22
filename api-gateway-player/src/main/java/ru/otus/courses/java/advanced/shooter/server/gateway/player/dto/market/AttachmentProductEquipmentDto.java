package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.AttachmentDto;

@Schema(name = "AttachmentProductEquipment", description = "Product equipment: attachment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentProductEquipmentDto implements ProductEquipmentDto {

    @Schema(description = "Attachment info")
    private AttachmentDto attachment;
}
