package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Schema(
        name = "AttachmentSaveRequest",
        description = "Create/update attachment request"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentSaveRequestDto {

    @Schema(description = "Enabled flag", example = "true")
    @Builder.Default
    private boolean enabled = true;

    @Schema(description = "Attachment name", example = "Red Dot Sight")
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Schema(description = "Attachment type", example = "SCOPE")
    @NotNull(message = "Type must be specified")
    private AttachmentType type;

    @Schema(description = "Attachment effect")
    @NotNull(message = "Effect must be specified")
    @Valid
    private AttachmentEffectDto effect;

    @Schema(description = "Compatible gun IDs", example = "[10, 11]")
    @NotNull
    @Builder.Default
    private List<@NotNull Integer> compatibleGunIds = new ArrayList<>();

    @Schema(
            name = "AttachmentEffect",
            description = "Attachment effect values (rates and distances)"
    )
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentEffectDto {

        @Schema(description = "Sound loudness rate", example = "0", minimum = "0")
        @Builder.Default
        private int soundLoudnessRate = 0;

        @Schema(description = "Max zoom rate", example = "0", minimum = "0")
        @Builder.Default
        private int maxZoomRate = 0;

        @Schema(description = "Blow back rate", example = "0", minimum = "0")
        @Builder.Default
        private int blowBackRate = 0;

        @Schema(description = "Laser max distance (meters)", example = "0", minimum = "0")
        @Min(value = 0, message = "Laser max distance must be non-negative")
        @Builder.Default
        private int laserMaxDistanceMeters = 0;

        @Schema(description = "Bullet speed rate", example = "0", minimum = "0")
        @Builder.Default
        private int bulletSpeedRate = 0;
    }
}
