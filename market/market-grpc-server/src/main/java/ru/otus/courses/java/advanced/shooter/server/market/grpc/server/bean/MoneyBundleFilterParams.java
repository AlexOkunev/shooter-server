package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.util.Set;

@Value
@Builder
@FieldNameConstants
public class MoneyBundleFilterParams {

    Boolean enabled;
    Boolean currencyEnabled;
    Boolean currencyCanBeBought;

    @Builder.Default
    Set<Integer> currencyIds = Set.of();
}
