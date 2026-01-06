package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@Builder
@FieldNameConstants
public class CurrencySavedData {

    @Builder.Default
    boolean enabled = true;

    @NotBlank(message = "Currency name cannot be blank or null")
    String name;

    @Builder.Default
    boolean canBeBought = true;

    @Builder.Default
    boolean canBeGivenAsAward = true;
}
