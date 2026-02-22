package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundleDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundlePageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.SaveMoneyBundleRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.CurrencyMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.helper.CurrencyMappingHelper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationInfoDtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleWritableData;

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
public abstract class MoneyBundleMapper {

    @Mapping(source = "data", target = "items")
    public abstract MoneyBundlePageResponseDto toPageDto(MoneyBundleInfoListPage page,
                                                         @Context CurrencyMappingContext context);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<MoneyBundleDto> toDtoList(Collection<MoneyBundleInfo> items,
                                                   @Context CurrencyMappingContext context);

    @Mapping(
            target = "currency",
            source = "currencyId",
            qualifiedByName = CurrencyMappingHelper.NamedMethods.NAMED_TO_CURRENCY_DTO
    )
    public abstract MoneyBundleDto toDto(MoneyBundleInfo item, @Context CurrencyMappingContext context);

    public abstract MoneyBundleWritableData toProto(SaveMoneyBundleRequestDto dto);
}