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
    private Integer soundLoudnessRate;

    private Integer maxZoomRate;

    private Integer blowBackRate;

    @Min(0)
    private Integer laserMaxDistanceMeters;

    private Integer bulletSpeedRate;
}
