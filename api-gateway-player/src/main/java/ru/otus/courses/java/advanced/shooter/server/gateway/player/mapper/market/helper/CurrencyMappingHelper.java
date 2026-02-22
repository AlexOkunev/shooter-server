package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.helper;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.mapstruct.Context;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.CurrencyInfo;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.CurrencyDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.CurrencyMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.CurrencyMappingContext;

@Component
@RequiredArgsConstructor
public class CurrencyMappingHelper {

    private final CurrencyMapper currencyMapper;


    @UtilityClass
    public static final class NamedMethods {
        public static final String NAMED_TO_CURRENCY_DTO = "toCurrencyDto";
    }

    @Named(NamedMethods.NAMED_TO_CURRENCY_DTO)
    public CurrencyDto toCurrencyDto(int currencyId, @Context CurrencyMappingContext context) {
        CurrencyInfo currencyInfo = context.currenciesById().get(currencyId);
        return currencyMapper.toDto(currencyInfo);
    }
}
