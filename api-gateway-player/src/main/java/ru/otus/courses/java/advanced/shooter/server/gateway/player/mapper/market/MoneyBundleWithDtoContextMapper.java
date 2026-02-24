package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.CurrencyDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.MoneyBundleDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.MoneyBundlePageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context.CurrencyDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationInfoDtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleInfoListPage;

import java.util.Collection;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class,
                PaginationInfoDtoMapper.class
        }
)
public abstract class MoneyBundleWithDtoContextMapper {

    @Mapping(source = "data", target = "items")
    public abstract MoneyBundlePageResponseDto toPageDto(MoneyBundleInfoListPage page,
                                                         @Context CurrencyDtoMappingContext context);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<MoneyBundleDto> toDtoList(Collection<MoneyBundleInfo> items,
                                                   @Context CurrencyDtoMappingContext context);

    @Mapping(
            target = "currency",
            source = "currencyId"
    )
    public abstract MoneyBundleDto toDto(MoneyBundleInfo item, @Context CurrencyDtoMappingContext context);

    protected CurrencyDto getCurrencyDto(int currencyId, @Context CurrencyDtoMappingContext context) {
        return context.currenciesById().get(currencyId);
    }
}