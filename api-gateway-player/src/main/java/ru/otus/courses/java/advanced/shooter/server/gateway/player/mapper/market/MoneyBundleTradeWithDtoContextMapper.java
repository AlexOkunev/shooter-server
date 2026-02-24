package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.CurrencyDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.MoneyBundleTradeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.MoneyBundleTradePageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.MoneyBundleTradeSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context.CurrencyDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationInfoDtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.GetMoneyBundleTradesRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeInfoListPage;

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
public abstract class MoneyBundleTradeWithDtoContextMapper {

    @Mapping(source = "data", target = "items")
    public abstract MoneyBundleTradePageResponseDto toPageDto(MoneyBundleTradeInfoListPage page,
                                                              @Context CurrencyDtoMappingContext context);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<MoneyBundleTradeDto> toDtoList(Collection<MoneyBundleTradeInfo> items,
                                                        @Context CurrencyDtoMappingContext context);

    @Mapping(
            target = "currency",
            source = "currencyId"
    )
    public abstract MoneyBundleTradeDto toDto(MoneyBundleTradeInfo item, @Context CurrencyDtoMappingContext context);

    public abstract GetMoneyBundleTradesRequest.Filter toProto(MoneyBundleTradeSearchRequestDto requestDto);

    protected CurrencyDto getCurrencyDto(int currencyId, @Context CurrencyDtoMappingContext context) {
        return context.currenciesById().get(currencyId);
    }
}