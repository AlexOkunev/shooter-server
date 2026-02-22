package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.PlayerAccountItemDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.PlayerAccountItemPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.CurrencyMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.helper.CurrencyMappingHelper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationInfoDtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemsPage;

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
public abstract class PlayerAccountItemMapper {

    @Mapping(source = "data", target = "items")
    public abstract PlayerAccountItemPageResponseDto toPageDto(PlayerAccountItemsPage playerInventoryLogPage,
                                                               @Context CurrencyMappingContext context);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<PlayerAccountItemDto> toDtoList(Collection<PlayerAccountItemInfo> items,
                                                         @Context CurrencyMappingContext context);

    @Mapping(
            target = "currency",
            source = "currencyId",
            qualifiedByName = CurrencyMappingHelper.NamedMethods.NAMED_TO_CURRENCY_DTO
    )
    public abstract PlayerAccountItemDto toDto(PlayerAccountItemInfo item, @Context CurrencyMappingContext context);
}