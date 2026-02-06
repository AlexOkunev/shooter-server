package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@Builder
@FieldNameConstants
public class MoneyBundleSavedData {

    @NotNull(message = "Money bundle currencyId cannot be null")
    Integer currencyId;

    @Positive(message = "Money bundle currencyAmount must be positive")
    int currencyAmount;

    @Positive(message = "Money bundle rublesPrice must be positive")
    int rublesPrice;

    @Builder.Default
    boolean enabled = true;
}
