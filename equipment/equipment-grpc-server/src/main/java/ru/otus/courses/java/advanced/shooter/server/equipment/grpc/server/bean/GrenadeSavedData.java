package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@Builder
@FieldNameConstants
public class GrenadeSavedData {

    @Builder.Default
    boolean enabled = true;

    @NotBlank(message = "Grenade name cannot be blank or null")
    String name;

    @Positive(message = "Blast damage radius meters must be > 0")
    int blastDamageRadiusMeters;

    @Min(value = 0, message = "Max blast damage HP must be >= 0")
    int maxBlastDamageHp;

    @Min(value = 0, message = "Max blind time ms must be >= 0")
    int maxBlindTimeMs;

    @Min(value = 0, message = "Max deaf time ms must be >= 0")
    int maxDeafTimeMs;
}
