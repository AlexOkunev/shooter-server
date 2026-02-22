package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AttachmentEffectDto;

@Schema(
        name = "AttachmentDto",
        description = "Attachment information"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentReducedDto {

    @Schema(description = "Attachment ID", example = "1")
    private Integer id;

    @Schema(description = "Attachment is enabled", example = "true")
    private Boolean enabled;

    @Schema(description = "Name", example = "Red Dot Sight")
    private String name;

    @Schema(description = "Attachment type", example = "SCOPE")
    private AttachmentType type;

    @Schema(description = "Attachment effect")
    private AttachmentEffectDto effect;
}
