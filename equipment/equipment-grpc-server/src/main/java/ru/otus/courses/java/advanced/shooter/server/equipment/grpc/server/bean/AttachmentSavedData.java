package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.util.Set;

@Value
@Builder
@FieldNameConstants
public class AttachmentSavedData {

    @Builder.Default
    boolean enabled = true;

    @NotBlank(message = "Attachment name cannot be blank or null")
    String name;

    @NotNull(message = "Attachment type cannot be null")
    AttachmentType type;

    @NotNull(message = "Attachment effect cannot be null")
    @Valid
    AttachmentSavedData.AttachmentEffect effect;

    @Builder.Default
    Set<Integer> compatibleGunIds = Set.of();

    /**
     * rate fields interpreted as percent-rate:
     * coeff = (100 + rate) / 100
     * a' = a * coeff
     */
    @Value
    @Builder
    @FieldNameConstants
    public static class AttachmentEffect {

        @Builder.Default
        @Min(value = -99, message = "soundLoudnessRate must be >= -99")
        int soundLoudnessRate = 0;

        @Builder.Default
        @Min(value = -99, message = "maxZoomRate must be >= -99")
        int maxZoomRate = 0;

        @Builder.Default
        @Min(value = -99, message = "blowBackRate must be >= -99")
        int blowBackRate = 0;

        @Builder.Default
        @Min(value = 0, message = "laserMaxDistanceMeters must be >= 0")
        int laserMaxDistanceMeters = 0;

        @Builder.Default
        @Min(value = -99, message = "bulletSpeedRate must be >= -99")
        int bulletSpeedRate = 0;
    }
}
