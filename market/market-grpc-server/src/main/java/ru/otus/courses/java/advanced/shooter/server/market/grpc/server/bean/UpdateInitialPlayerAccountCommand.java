package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UpdateInitialPlayerAccountCommand {

    @Builder.Default
    List<@Valid @NotNull SavedInitialPlayerAccountItem> savedItems = List.of();

    @Builder.Default
    List<@NotNull Integer> deletedCurrencyIds = List.of();
}
