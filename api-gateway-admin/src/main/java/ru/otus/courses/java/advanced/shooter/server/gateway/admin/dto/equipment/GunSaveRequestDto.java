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
        name = "GunSaveRequest",
        description = "Create/update gun request"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GunSaveRequestDto {

    @Schema(description = "Enabled flag", example = "true")
    @Builder.Default
    private boolean enabled = true;

    @Schema(description = "Gun name", example = "AK-74")
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Schema(description = "Gun type", example = "ASSAULT_RIFLE")
    @NotNull(message = "Type must be specified")
    private GunType type;

    @Schema(description = "Weight (grams)", example = "3400", minimum = "0")
    @Min(value = 1, message = "Weight must be positive")
    private int weightGrams;

    @Schema(description = "Rate of fire (per minute)", example = "600", minimum = "0")
    @Min(value = 0, message = "Rate of fire must be non-negative")
    @Builder.Default
    private int rateOfFirePerMinute = 0;

    @Schema(description = "Compatible ammunition IDs", example = "[1, 2, 3]")
    @NotNull
    @Builder.Default
    private List<@NotNull Integer> compatibleAmmunitionIds = new ArrayList<>();

    @Schema(description = "Compatible attachment IDs", example = "[10, 11]")
    @NotNull
    @Builder.Default
    private List<@NotNull Integer> compatibleAttachmentIds = new ArrayList<>();
}
