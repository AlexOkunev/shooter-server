package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context;

import lombok.Builder;
import org.mapstruct.Context;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.CurrencyDto;

import java.util.Map;

@Builder
public record CurrencyDtoMappingContext(
        @Context Map<Integer, CurrencyDto> currenciesById
) {

}
