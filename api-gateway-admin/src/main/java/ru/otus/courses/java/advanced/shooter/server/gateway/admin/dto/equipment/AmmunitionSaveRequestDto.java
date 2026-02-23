package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
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
        name = "AmmunitionSaveRequest",
        description = "Create/update ammunition request"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmmunitionSaveRequestDto {

    @Schema(description = "Enabled flag", example = "true")
    @Builder.Default
    private boolean enabled = true;

    @Schema(description = "Ammunition name", example = "9x19 FMJ")
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Schema(description = "Speed", example = "380", minimum = "1")
    @Min(value = 1, message = "Speed must be positive")
    @Builder.Default
    private int speed = 1;

    @Schema(description = "Damage mean value", example = "30", minimum = "1")
    @Min(value = 1, message = "Damage mean value must be positive")
    @Builder.Default
    private int damageMeanValue = 1;

    @Schema(description = "Damage variance", example = "5", minimum = "1")
    @Min(value = 1, message = "Damage variance must be positive")
    @Builder.Default
    private int damageVariance = 1;

    @Schema(description = "Compatible gun IDs", example = "[10, 11]")
    @NotNull
    @Builder.Default
    private List<@NotNull Integer> compatibleGunIds = new ArrayList<>();
}
