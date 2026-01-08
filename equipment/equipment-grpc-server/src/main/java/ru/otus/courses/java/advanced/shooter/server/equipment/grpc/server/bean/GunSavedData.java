package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.util.Set;

@Value
@Builder
@FieldNameConstants
public class GunSavedData {

    @Builder.Default
    boolean enabled = true;

    @NotBlank(message = "Gun name cannot be blank or null")
    String name;

    @NotNull(message = "Gun type cannot be null")
    GunType type;

    @Positive(message = "Gun weightGrams must be positive")
    int weightGrams;

    @Positive(message = "Gun rateOfFirePerMinute must be positive")
    int rateOfFirePerMinute;

    @Builder.Default
    Set<Integer> compatibleAmmunitionIds = Set.of();

    @Builder.Default
    Set<Integer> compatibleAttachmentIds = Set.of();
}
