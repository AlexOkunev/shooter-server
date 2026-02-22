package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "AttachmentEffect",
        description = "Attachment effect parameters"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentEffectDto {

    @Schema(description = "Sound loudness rate (percent points)", example = "-20")
    private int soundLoudnessRate;

    @Schema(description = "Maximum zoom rate (percent points)", example = "50")
    private int maxZoomRate;

    @Schema(description = "Blow-back rate (percent points)", example = "-10")
    private int blowBackRate;

    @Schema(description = "Laser max distance in meters", example = "30")
    private int laserMaxDistanceMeters;

    @Schema(description = "Bullet speed rate (percent points)", example = "5")
    private int bulletSpeedRate;
}
