package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context;

import lombok.Builder;
import org.mapstruct.Context;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencyDto;

import java.util.Map;

@Builder
public record CurrencyDtoMappingContext(
        @Context Map<Integer, CurrencyDto> currenciesById
) {

}
