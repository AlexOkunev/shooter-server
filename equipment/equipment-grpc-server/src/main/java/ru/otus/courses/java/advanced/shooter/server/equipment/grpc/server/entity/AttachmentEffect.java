package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AttachmentEffect {
    @Min(-99)
    private Integer soundLoudnessRate = 0;

    @Min(-99)
    private Integer maxZoomRate = 0;

    @Min(-99)
    private Integer blowBackRate = 0;

    @Min(0)
    private Integer laserMaxDistanceMeters = 0;

    @Min(-99)
    private Integer bulletSpeedRate = 0;
}
