package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.initial;

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
        name = "UpdateInitialPlayerInventoryRequest",
        description = "Update request for initial player inventory"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInitialPlayerInventoryRequestDto {

    @Schema(description = "Items to save/update")
    @NotNull
    @Builder.Default
    private List<@Valid @NotNull SavedInitialPlayerInventoryItemDto> savedItems = new ArrayList<>();

    @Schema(description = "Items to delete")
    @NotNull
    @Builder.Default
    private List<@Valid @NotNull DeletedInitialPlayerInventoryItemDto> deletedItems = new ArrayList<>();
}
