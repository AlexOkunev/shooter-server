package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "CurrencySaveRequest",
        description = "Create/update currency request"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencySaveRequestDto {

    @Schema(description = "Currency name", example = "Gold")
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Schema(description = "Enabled flag", example = "true")
    @Builder.Default
    private boolean enabled = true;

    @Schema(description = "Can be bought flag", example = "true")
    @Builder.Default
    private boolean canBeBought = true;

    @Schema(description = "Can be given as award flag", example = "true")
    @Builder.Default
    private boolean canBeGivenAsAward = true;
}
