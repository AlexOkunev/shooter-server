package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.time.ZonedDateTime;
import java.util.Set;

@Value
@Builder
@FieldNameConstants
public class CurrencyFilterParams {

    Boolean enabled;
    String name;
    Boolean canBeBought;
    Boolean canBeGivenAsAward;
    ZonedDateTime updatedAfter;

    @Builder.Default
    Set<Integer> currencyIds = Set.of();
}
