package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.initial;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Schema(
        name = "UpdateInitialPlayerAccountRequest",
        description = "Update request for initial player account"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInitialPlayerAccountRequestDto {

    @Schema(description = "Items to save/update")
    @NotNull
    @Builder.Default
    private List<@Valid @NotNull SavedInitialPlayerAccountItemDto> savedItems = new ArrayList<>();

    @Schema(description = "Currency ids to delete")
    @NotNull
    @Builder.Default
    private List<@NotNull Integer> deletedCurrencyIds = new ArrayList<>();
}
