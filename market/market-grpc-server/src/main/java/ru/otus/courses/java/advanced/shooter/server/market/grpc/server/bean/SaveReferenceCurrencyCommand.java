package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@Builder
@FieldNameConstants
public class SaveReferenceCurrencyCommand {

    @NotNull
    Integer id;

    @NotNull
    String name;

    @NotNull
    @Builder.Default
    Boolean enabled = true;

    @NotNull
    @Builder.Default
    Boolean canBeBought = true;
}
