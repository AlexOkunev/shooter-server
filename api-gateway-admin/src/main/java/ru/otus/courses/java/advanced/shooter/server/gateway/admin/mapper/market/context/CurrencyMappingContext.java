package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context;

import lombok.Builder;
import org.mapstruct.Context;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.CurrencyInfo;

import java.util.Map;

@Builder
public record CurrencyMappingContext(
        @Context Map<Integer, CurrencyInfo> currenciesById
) {

}
