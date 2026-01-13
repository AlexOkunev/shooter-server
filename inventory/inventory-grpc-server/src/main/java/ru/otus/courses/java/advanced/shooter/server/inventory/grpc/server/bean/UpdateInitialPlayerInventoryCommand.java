package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UpdateInitialPlayerInventoryCommand {

    @Builder.Default
    List<@Valid @NotNull SavedInitialPlayerInventoryItem> savedItems = List.of();

    @Builder.Default
    List<@Valid @NotNull DeletedInitialPlayerInventoryItem> deletedItems = List.of();
}
