package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.util.Set;

@Value
@Builder
@FieldNameConstants
public class AmmunitionSavedData {

    @Builder.Default
    boolean enabled = true;

    @NotBlank(message = "Ammunition name cannot be blank or null")
    String name;

    @Positive(message = "Ammunition speed must be positive")
    int speed;

    @Positive(message = "Damage mean value must be positive")
    int damageMeanValue;

    @Positive(message = "Damage variance must be positive")
    int damageVariance;

    @Builder.Default
    Set<Integer> compatibleGunIds = Set.of();
}
