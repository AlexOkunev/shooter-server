package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "GrenadeSaveRequest",
        description = "Create/update grenade request"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrenadeSaveRequestDto {

    @Schema(description = "Enabled flag", example = "true")
    @Builder.Default
    private boolean enabled = true;

    @Schema(description = "Grenade name", example = "Flash")
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Schema(description = "Blast damage radius (meters)", example = "5", minimum = "0")
    @Min(value = 0, message = "Blast damage radius must be non-negative")
    @Builder.Default
    private int blastDamageRadiusMeters = 0;

    @Schema(description = "Max blast damage (HP)", example = "100", minimum = "0")
    @Min(value = 0, message = "Max blast damage must be non-negative")
    @Builder.Default
    private int maxBlastDamageHp = 0;

    @Schema(description = "Max blind time (ms)", example = "1500", minimum = "0")
    @Min(value = 0, message = "Max blind time must be non-negative")
    @Builder.Default
    private int maxBlindTimeMs = 0;

    @Schema(description = "Max deaf time (ms)", example = "1500", minimum = "0")
    @Min(value = 0, message = "Max deaf time must be non-negative")
    @Builder.Default
    private int maxDeafTimeMs = 0;
}
