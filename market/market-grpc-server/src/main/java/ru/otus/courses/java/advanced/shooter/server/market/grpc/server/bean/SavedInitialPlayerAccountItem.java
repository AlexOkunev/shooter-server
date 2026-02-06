package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@Builder
@FieldNameConstants
public class SavedInitialPlayerAccountItem {
    @NotNull
    Integer currencyId;

    @Positive
    Integer amount;

    @Min(0)
    Integer version;

    @NotNull
    @Builder.Default
    Boolean enabled = true;
}
