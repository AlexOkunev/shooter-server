package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundleTradeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundleTradePageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundleTradeSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.CurrencyMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.helper.CurrencyMappingHelper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationInfoDtoMapper;
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
                PaginationInfoDtoMapper.class,
                CurrencyMappingHelper.class
        }
)
public abstract class MoneyBundleTradeMapper {

    @Mapping(source = "data", target = "items")
    public abstract MoneyBundleTradePageResponseDto toPageDto(MoneyBundleTradeInfoListPage page,
                                                              @Context CurrencyMappingContext context);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<MoneyBundleTradeDto> toDtoList(Collection<MoneyBundleTradeInfo> items,
                                                        @Context CurrencyMappingContext context);

    @Mapping(
            target = "currency",
            source = "currencyId",
            qualifiedByName = CurrencyMappingHelper.NamedMethods.NAMED_TO_CURRENCY_DTO
    )
    public abstract MoneyBundleTradeDto toDto(MoneyBundleTradeInfo item, @Context CurrencyMappingContext context);

    public abstract GetMoneyBundleTradesRequest.Filter toProto(MoneyBundleTradeSearchRequestDto requestDto);
}